package com.web.my.loan.app.controller;

import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.services.MailServices;
import com.web.my.loan.app.services.UserServices;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/loan")
public class HomeController {

    @Autowired
    private UserServices userService;

    @Autowired
    private MailServices mailServices;

    private Message message;

    @GetMapping("/signup")
    public String gosignup(Model model) {
        model.addAttribute("title", "SignUp");
        model.addAttribute("user", new User());

        return "Signup";
    }

    @PostMapping("/do_signup")
    public String doSignUp(
            @Valid
            @ModelAttribute User user,
            BindingResult result,
            @RequestParam("otp") String otp,
            HttpSession session
    ) {

        if (result.hasErrors()) {
            return "Signup";
        }
        this.message = this.userService.saveUser(user, Integer.parseInt(otp));
        session.setAttribute("msg", message);

        if (message.getType().equals("alert-danger")) {
            return "Signup";
        }
        return "redirect:/loan/signin";
    }

    @GetMapping("/signin")
    public String goSignin(Model model) {
        model.addAttribute("title", "Sign-In");
        return "templates.Signin";
    }

    @GetMapping("/home")
    public String goHome(Model model) {
        model.addAttribute("title", "Home-Page");
        return "home";
    }

    @GetMapping("/send-Otp/{email}")
    public String otpSend(
            Model model,
            @PathVariable("email") String email
    ) throws MessagingException {

        model.addAttribute("title", "Home-Page");
        mailServices.sendOtp(email, "");
        return "redirect:/loan/signup";
    }

    @GetMapping("/login")
    public String gosignin(@RequestParam("logout") String log, Model model) {

        if (log != null) {
            model.addAttribute("mess", "Logout Successfull");
        }

        return "Signin";
    }

}
