package com.tapmovie.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tapmovie.authentication.entities.ForgotPassword;
import com.tapmovie.authentication.entities.User;
import com.tapmovie.authentication.repositories.ForgotPasswordRepository;
import com.tapmovie.authentication.repositories.UserRepository;
import com.tapmovie.authentication.utils.ChangePassword;
import com.tapmovie.dto.MailBody;
import com.tapmovie.service.EmailService;

/**
 * The ForgotPasswordController class is responsible for handling
 * requests related to the "forgot password" functionality.
 * It provides endpoints for verifying user email, generating and 
 * verifying OTPs, and changing the user's password.
 */
@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a ForgotPasswordController with the specified dependencies.
     *
     * @param userRepository         the repository for user entities
     * @param emailService           the service for sending emails
     * @param forgotPasswordRepository the repository for handling forgot password entries
     * @param passwordEncoder        the password encoder for securely hashing passwords
     */
    public ForgotPasswordController(UserRepository userRepository, EmailService emailService,
            ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifies the user's email and generates an OTP for password recovery.
     *
     * @param email the email address of the user
     * @return a ResponseEntity containing a success message
     */
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        Optional<ForgotPassword> existingEntry = forgotPasswordRepository.findByUser(user);
        int otp = otpGenerator();

        if (existingEntry.isPresent()) {
            // Update the existing entry with new OTP and expiration time
            ForgotPassword forgotPassword = existingEntry.get();
            forgotPassword.setOtp(otp);
            forgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 70 * 1000));
            forgotPasswordRepository.save(forgotPassword);
        } else {
            // Create a new entry for forgot password
            ForgotPassword forgotPassword = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                    .user(user)
                    .build();
            forgotPasswordRepository.save(forgotPassword);
        }

        // Send email with OTP
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP (One Time Password) for your forgot password request: " + otp)
                .subject("OTP for forgot password request")
                .build();
        emailService.sendSimpleMessage(mailBody);

        return ResponseEntity.ok("Email sent for verification");
    }

    /**
     * Verifies the OTP provided by the user.
     *
     * @param otp   the OTP entered by the user
     * @param email the email address of the user
     * @return a ResponseEntity containing the result of the verification
     */
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getForgotPasswordId());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("OTP has expired");
        }

        // If OTP is valid, return a success message
        return ResponseEntity.ok("OTP is verified");
    }

    /**
     * Changes the user's password after successful OTP verification.
     *
     * @param changePassword the request containing the new password and repeat password
     * @param email          the email address of the user
     * @return a ResponseEntity containing the result of the password change
     */
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
            @PathVariable String email) {
        
        String password = changePassword.password().trim();
        String repeatPassword = changePassword.repeatPassword().trim();

        // Log the password values for debugging
        System.out.println("Password: '" + password + "'");
        System.out.println("Repeat Password: '" + repeatPassword + "'");

        if (!Objects.equals(password, repeatPassword)) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Please enter the password again!");
        }

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password changed successfully!");
    }

    /**
     * Generates a random OTP for password recovery.
     *
     * @return a randomly generated OTP
     */
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
