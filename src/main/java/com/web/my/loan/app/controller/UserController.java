package com.web.my.loan.app.controller;

import com.web.my.loan.app.entity.Address;
import com.web.my.loan.app.entity.ApprovedLoan;
import com.web.my.loan.app.entity.BorrowedLoan;
import com.web.my.loan.app.entity.Loan;
import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.helper.Helper;
import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.services.LoanServices;
import com.web.my.loan.app.services.MailServices;
import com.web.my.loan.app.services.UserServices;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserServices userService;
    private LoanServices loanService;
    private MailServices mailServices;


    private Message message = null;
    private List<ApprovedLoan> Aloan = null;
    private User user = null;
    private Loan loan = null;
    private List<Loan> loans = null;
    private List<Address> addresses = null;
    private Address address = null;
    private List<BorrowedLoan> Bloan = null;

    public UserController(UserServices userService, LoanServices loanService, MailServices mailServices) {
        this.userService = userService;
        this.loanService = loanService;
        this.mailServices = mailServices;
    }

    @GetMapping("/dash")
    @PreAuthorize("hasRole('USER')")
    public String goDash() {

        return "normal/userDash";
    }

    // Go to the Add Loan Request Page
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/addloan")
    public String goAddloan(Model model, Principal principal) {

        this.user = this.userService.getUser(principal.getName());

        model.addAttribute("title", "Request-Loan");
        model.addAttribute("user", user);
        return "normal/loanRequest";
    }

    // Show Profile
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/showProfile")
    public String showProfile(Model model, Principal principal) {
        model.addAttribute("title", "Show-Profile");
        user = this.userService.getUser(principal.getName());
        addresses = user.getAddress();
        model.addAttribute("user", user);
        model.addAttribute("address", addresses);
        return "normal/profile";
    }

    // Add new Address
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/address")
    public String goaddress(Model model) {
        model.addAttribute("title", "Address");
        return "normal/addAddress";
    }

    // Proceed new Address
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/doAddress")
    public String addAddress(
            Model model,
            Address address,
            Principal principal,
            HttpSession session) {

        model.addAttribute("title", "Address");
        this.user = this.userService.getUser(principal.getName());
        this.message = this.userService.addAddress(address, userService.getUser(principal.getName()));

        session.setAttribute("msg", message);

        return "redirect:/user/showProfile";
    }

    // Update Address
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/updateaddress")
    public String updateAddress(
            @RequestParam("index") int id,
            Model model,
            Principal principal) {

        model.addAttribute("title", "Update Address");

        user = this.userService.getUser(principal.getName());
        address = this.userService.getUpdateAddress(user, id);
        model.addAttribute("address", address);
        return "normal/updateAddress";
    }

    // Process Update Data
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/doUpAddress")
    public String doProcessAddress(
            @ModelAttribute Address address,
            Model model,
            Principal principal,
            HttpSession session) {

        model.addAttribute("title", "Process-Address");
        user = this.userService.getUser(principal.getName());
        this.message = userService.updateAddress(address, user);
        session.setAttribute("msg", message);

        return "redirect:/user/showProfile";
    }

    // Update Profile Detail
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/updateProfile")
    public String updateProfile(Model model, Principal principal) {
        model.addAttribute("title", "Update Profile");
        user = userService.getUser(principal.getName());
        model.addAttribute("user", user);
        return "normal/updateProfile";
    }

    // Process Update Profile Detail
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/do-updateProfile")
    public String processUpdateProfile(
            @ModelAttribute User user,
            Model model,
            Principal principal,
            HttpSession session) {

        model.addAttribute("title", "Update Profile");
        this.user = userService.getUser(principal.getName());
        this.message = this.userService.updateProfile(user, this.user);
        session.setAttribute("msg1", message);
        return "redirect:/user/showProfile";
    }

    // Handle Remove Address Request
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/removeAddress/{index}")
    public String removeAddress(
            @PathVariable("index") int id,
            Principal principal,
            HttpSession session) {
        this.user = userService.getUser(principal.getName());

        this.message = this.userService.removeAddress(user, id);
        session.setAttribute("msg", message);
        return "redirect:/user/showProfile";
    }

    // All Requests Data
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/allRes")
    public String allRes(Model model) {
        model.addAttribute("title", "All-Request");
        this.loans = this.loanService.getloan();
        model.addAttribute("loans", loans);

        return "normal/Allres";
    }

    // My Request Data
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myRes")
    public String myRes(
            Model model,
            Principal principal) {

        model.addAttribute("title", "My-Request");

        this.user = userService.getUser(principal.getName());
        this.Bloan = user.getBorrowList();
        model.addAttribute("loans", Bloan);

        return "normal/myRes";
    }

    // Approves Request Data
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/appRes")
    public String appRes(Model model, Principal principal) {
        model.addAttribute("title", "Approve-Request");

        this.user = userService.getUser(principal.getName());
        this.Aloan = user.getApproveList();
        model.addAttribute("loans", Aloan);

        return "normal/approveRequest";
    }

    // Process Loan Data
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/processLoan")
    public String processLoan(
            Model model,
            @ModelAttribute Loan loan,
            Principal principal,
            HttpSession session) {

        model.addAttribute("title", "Process-loan");
        this.user = userService.getUser(principal.getName());
        message = this.loanService.saveLoan(loan, user);

        session.setAttribute("msg", message);
        return "redirect:/user/myRes";
    }

    // Approved The loan request
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/approve")
    public String approveLoan(
            @RequestParam("id") int id,
            Model model,
            Principal principal,
            HttpSession session
    ) {
        model.addAttribute("title", "Approved-loan");

        this.message = this.userService.approveRes(id, principal);
        session.setAttribute("msg", message);

        if (message.getType().equals("alert-warning")) {

            return "redirect:/user/allRes";
        }
        return "redirect:/user/appRes";
    }

    // Delete Request
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/deleteRes")
    public String deleteRes(
            @RequestParam("id") int id,
            Principal principal,
            HttpSession session) {

        this.message = this.userService.deleteRes(id, userService.getUser(principal.getName()));
        session.setAttribute("msg", message);
        return "redirect:/user/myRes";
    }

    // Go Specific Page
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/goSpecific")
    public String goSpecific(
            Model model,
            HttpSession session) {

        model.addAttribute("loan", this.loan);
        model.addAttribute("type", session.getAttribute("type"));
        return "normal/DetailLoan";
    }

        
    // Checking The Page 
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/specific/{id}/{app}")
    public String goSpecific(
            @PathVariable("id") int id,
            @PathVariable("app") String type,
            Model model,
            HttpSession session
    ) {

        this.loan = this.loanService.getLoanById(id);
        // Remove Already select Value
        session.removeAttribute("type");
        session.setAttribute("type", type);
        if(this.loan.getLoanApproval().equals("Yes")){
            
        return "redirect:/user/approvedRes";
        }
        return "redirect:/user/goSpecific";
    }

        // Go Specific Page
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/approvedRes")
    public String approvedDetail(
            Model model,
            HttpSession session) {

        model.addAttribute("loan", this.loan);
        model.addAttribute("type", session.getAttribute("type"));
        return "normal/afterApproveDetail";
    }

    
    // Specific Approval Detail
    @PreAuthorize("hasRole('USER')")
        @GetMapping("/approveUser")
    public String approveDetail(
            Model model,
            @RequestParam("id") int id) {

        this.loan = this.loanService.getLoanById(id - 5);
        user = this.loanService.getdetailofUser(loan);

        model.addAttribute("user", user);
        model.addAttribute("loan", this.loan);
        return "normal/approvalDetail";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/goSetting")
    public String goSetting(
            Model model,
            Principal principal) {
        this.user = this.userService.getUser(principal.getName());

        model.addAttribute("title", "Setting");
        model.addAttribute("user", user);

        return "normal/setting";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mail")
    public String goMessage(
            Model model,
            @RequestParam("name") String email
    ) {

        model.addAttribute("title", "Send_mail");
        model.addAttribute("email", email + "@gmail.com");
        return "normal/SendMail";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/mailcontent")
    public String sendMail(
            Model model,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("email") String to,
            HttpSession session
    ) {

        model.addAttribute("title", "Send_mail");
        this.message = this.mailServices.sendMail(to, title, content);
        session.setAttribute("msg", message);
        if (message.getType().equals("alert-danger")) {

            return "normal/SendMail";
        }
        return "normal/SendMail";
    }

    // Change Password
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/changePass")
    public String changePass(
            Model model,
            @RequestParam("currentPass") String currentPass,
            @RequestParam("newPass") String newPass,
            Principal principal,
            HttpSession session) {

        this.user = userService.getUser(principal.getName());
        this.message = this.userService.changePassword(currentPass, newPass, user);
        session.setAttribute("msg", message);

        return "redirect:/user/goSetting";
    }

    // Verify Email
    @PreAuthorize("hasRole('USER'")
    @GetMapping("/verify-email")
    public String verifyEmail(
            Model model,
            @RequestParam("currentEmail") String currentEmail,
            @RequestParam("newEmail") String newEmail,
            Principal principal,
            HttpSession session) {

        this.message = this.userService.VerifyEmail(currentEmail, newEmail, principal.getName(), "email");
        model.addAttribute("email", newEmail);
        session.setAttribute("msg1", message);

        if (message.getType().equals("alert-danger")) {
            return "redirect:/user/goSetting";
        }

        return "normal/verifyEmail";
    }

    // Verify Otp And Password Of user
    @PreAuthorize("hasRole('USER'")
    @PostMapping("/verify-otp")
    public String verifyOtp(
            Model model,
            @RequestParam("otp") int otp,
            @RequestParam("userName") String email,
            @RequestParam("password") String password,
            Principal principal,
            HttpSession session) {

        this.user = this.userService.getUser(principal.getName());
        this.message = this.userService.verifyEmailPass(email, otp, password, this.user);
        session.setAttribute("msg1", message);

        if (message.getType().equals("alert-danger")) {
            return "normal/verifyEmail";
        }

        return "redirect:/logout";

    }

}
