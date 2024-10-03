package com.tapmovie.dto;

/**
 * The MailBody class represents the structure of an email message.
 * It contains fields for the recipient's email address, the email subject,
 * and the email text/body. The class is immutable and utilizes a builder pattern
 * for easier construction of instances.
 *
 * @param to      the recipient's email address
 * @param subject the subject of the email
 * @param text    the body of the email
 */
public record MailBody(String to, String subject, String text) {

    /**
     * Static builder method to create a MailBodyBuilder instance.
     *
     * @return a new instance of MailBodyBuilder
     */
    public static MailBodyBuilder builder() {
        return new MailBodyBuilder();
    }

    /**
     * Static inner builder class for constructing MailBody instances.
     */
    public static class MailBodyBuilder {
        private String to;
        private String subject;
        private String text;

        /**
         * Sets the recipient's email address.
         *
         * @param to the recipient's email address
         * @return the current MailBodyBuilder instance
         */
        public MailBodyBuilder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * Sets the subject of the email.
         *
         * @param subject the subject of the email
         * @return the current MailBodyBuilder instance
         */
        public MailBodyBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the body text of the email.
         *
         * @param text the body of the email
         * @return the current MailBodyBuilder instance
         */
        public MailBodyBuilder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Builds and returns a new MailBody instance with the specified values.
         *
         * @return a new MailBody instance
         */
        public MailBody build() {
            return new MailBody(to, subject, text);
        }
    }
}
