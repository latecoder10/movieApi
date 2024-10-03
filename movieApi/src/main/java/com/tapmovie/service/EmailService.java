package com.tapmovie.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tapmovie.dto.MailBody;

/**
 * Service class for handling email sending functionality.
 * Utilizes JavaMailSender to send simple email messages.
 */
@Service
public class EmailService {
    
    private final JavaMailSender javaMailSender; // JavaMailSender instance for sending emails

    /**
     * Constructor to initialize the EmailService with a JavaMailSender instance.
     *
     * @param javaMailSender An instance of JavaMailSender for sending emails.
     */
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    /**
     * Sends a simple email message using the provided MailBody object.
     *
     * @param mailBody A MailBody object containing the recipient's email address, subject, and message body.
     */
    public void sendSimpleMessage(MailBody mailBody) {
        // Create a new SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Set the recipient's email address
        message.setTo(mailBody.to());
        
        // Set the sender's email address (ensure this is a valid email for your SMTP server)
        message.setFrom("palayan785@gmail.com");
        
        // Set the subject of the email
        message.setSubject(mailBody.subject());
        
        // Set the text body of the email
        message.setText(mailBody.text());
        
        // Send the email using the JavaMailSender instance
        javaMailSender.send(message);
    }
}
