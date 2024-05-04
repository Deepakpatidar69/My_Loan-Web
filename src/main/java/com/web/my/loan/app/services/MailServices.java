package com.web.my.loan.app.services;

import com.web.my.loan.app.helper.Helper;
import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.mail.MailSender;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServices {

    @Autowired
    private MailSender mailSender;

    private String message;
    private String type;

    public Message sendMail(String to, String title, String message) {

        try {

            this.mailSender.sendMail(message, title, to);
            this.message = "Message Send Successfully";
            type = "alert-success";
        } catch (Exception e) {

            this.message = "INTERNAL_SERVER_ERROR try again later";
            type = "alert-danger";

        }
        return new Message(this.message, type);
    }


    public Message sendOtp(String to , String regarding) throws MessagingException {
        int otp = Helper.otpGenerator();
        try {

            mailSender.sendOTP(otp, to , regarding);
            message = "Otp Send Successfully";
            type = "alert-success";
        } catch (MessagingException e) {

            message = "INTERNAL_SERVER_ERROR try again later";
            type = "alert-danger";
        }
        return new Message(message, type);
    }

}
