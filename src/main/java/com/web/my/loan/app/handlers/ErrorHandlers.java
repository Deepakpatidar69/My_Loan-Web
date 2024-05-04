package com.web.my.loan.app.handlers;

import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.helper.Message;
import org.springframework.dao.DataIntegrityViolationException;


public class ErrorHandlers {
    
    static String type = "alert-danger";
    static String message;
    
    public static Message ispresentorNot(Exception exception , User user) {

        try {
            DataIntegrityViolationException e = (DataIntegrityViolationException) exception;


            if ((e.getMostSpecificCause().getMessage()).contains(user.getUserName())) {
                message = "email is already exist!";
                return new Message(message, type);
                
            } else if ((e.getMostSpecificCause().getMessage()).contains(user.getMobileNumber())) {
                message = "mobile number is already exist!!";
                return new Message(message, type);
            } else {
                message = "Something Went Wrong INTERNAL_SERVER_ERROR Please Try again later some time ";
                return new Message(message, type);
            }

        } catch (Exception e2) {

           message = "Something Went Wrong INTERNAL_SERVER_ERROR Please Try again later some time";
           return new Message(message, type);
            }
    
    }

}
