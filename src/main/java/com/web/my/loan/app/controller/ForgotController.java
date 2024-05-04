package com.web.my.loan.app.controller;

import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.services.ForgotServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/forgot")
public class ForgotController {

    @Autowired
    private ForgotServices forgotService;

    private Message message;

    @GetMapping("/forgotPage")
    public String fillEmail(
            Model model
    ) {
        model.addAttribute("title", "forgot-Pass");

        return "forgot/forgotPage";
    }

    @PostMapping("/verify-mail")
    public String verify(
            @RequestParam("email") String email,
            Model model
    ) {
        model.addAttribute("title", "Home-Page");
        message = forgotService.sendotp(email , "forgot");
        model.addAttribute("msg", message);

        if (message.getType().equals("alert-danger")) {

            return "forgot/forgotPage";
        }
        return "forgot/VerifyMail";
    }

    @PostMapping("/verify_otp")
    public String verifyOtp(
            @RequestParam("otp") int otp,
            Model model
    ) {

        if (forgotService.matchOtp(otp)) {
            return "forgot/ChangePass";
        }

        model.addAttribute("msg", new Message("Otp does't Match please write carefully", "alert-danger"));
        return "forgot/VerifyMail";

    }

    @PostMapping("/change")
    public String changePass(
            @RequestParam("newpass") String newPass,
            @RequestParam("repass") String rePass,
            Model model) {

        System.out.println("New pass is : " + newPass);
        System.out.println("Re pass is : " + rePass);

        message = this.forgotService.changePass(newPass, rePass);

        model.addAttribute("msg", message);
        if (message.getType().equals("alert-danger")) {
            return "forgot/ChangePass";
        }

        return "Signin";
    }

}
