package com.web.my.loan.app.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

    @Autowired
    JavaMailSender javaMailSender;

    public String sendMail(String message, String subject, String to) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(message, "text/html");

        javaMailSender.send(mimeMessage);

        return "send";
    }

    public String sendOTP(int Otp, String to, String regarding) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
        mimeMessage.setSubject("From Easy Loan Approval!! ");
        
        switch (regarding) {
            case "email" -> mimeMessage.setContent("your otp for regarding Change Email is (" + Otp + ") here, do not share Any one this password", "text/html");
            case "forgot" -> mimeMessage.setContent("your otp for regarding forgot password is (" + Otp + ") here, do not share Any one this password", "text/html");
            default -> mimeMessage.setContent("Your Signup Otp is (" + Otp + ") do not share Any one this password", "text/html");
        }

        javaMailSender.send(mimeMessage);

        return "send";
    }

    public String changeEmail(int Otp, String to) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
        mimeMessage.setSubject("From Easy Loan Approval!! ");

        javaMailSender.send(mimeMessage);

        return "send";
    }

}
