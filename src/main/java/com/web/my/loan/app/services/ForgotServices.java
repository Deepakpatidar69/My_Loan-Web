package com.web.my.loan.app.services;

import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.helper.Helper;
import com.web.my.loan.app.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotServices {

    @Autowired
    private UserServices userService;

    @Autowired
    private Helper helper;

    @Autowired
    private MailServices mailServices;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User user = null;
    String message = "";
    String type = "";

    // Check the Email are Exist Or Not
    public boolean emailExist(String email) {

        this.user = this.userService.getUser(email);

        return user != null;
    }

    // Match the Otp or Not
    public boolean matchOtp(int otp) {

        return helper.isMatch(otp);
    }

    // Send Otp
    public Message sendotp(String email , String regarding) {

        type = "alert-danger";

        try {
            if (emailExist(email)) {
                
                mailServices.sendOtp(email , regarding);
                message = "Otp Send to " + email;
                type = "alert-success";
                return new Message(message, type);
            }
            message = "this username does't exist!!";
        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR try again later!!";
        }

        return new Message(message, type);
    }
    

    // Change Password
    public Message changePass(String newpass, String rePass) {

        if (!newpass.equals(rePass)) {

            return new Message("Both Password does't Match", "alert-danger");
        }

        try {
            this.user.setPassword(this.passwordEncoder.encode(newpass));
            if(this.userService.updateUser(user)){
            return new Message("Password Change SuccessFully", "alert-success");
            }
            
            throw new Exception();
        } catch (Exception e) {

            return new Message("INTERNAL_SERVER_ERROR trt again later", "alert-danger");

        }
    }

}
