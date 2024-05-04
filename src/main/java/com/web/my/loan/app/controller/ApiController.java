package com.web.my.loan.app.controller;

import com.web.my.loan.app.entity.Loan;
import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.services.LoanServices;
import com.web.my.loan.app.services.UserServices;
import java.security.Principal;
import java.util.List;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loanApi/loan")
public class ApiController {

    private UserServices userServices;
    private LoanServices loanServices;
    private User user;
    private Loan loan;

    private List<Loan> listLoan;

    public ApiController(UserServices userServices, LoanServices loanServices) {
        this.userServices = userServices;
        this.loanServices = loanServices;
    }

    @GetMapping("/get/loans")
    public ResponseEntity<List<Loan>> getData() {

        // this.user = this.userServices.getUser(principal.getName());
        listLoan = this.loanServices.getloan();

        return ResponseEntity.ok().body(listLoan);
    }

    @PostMapping("/add/loan")
    public ResponseEntity<Loan> addLoan(@RequestBody Loan loan, Principal principal) {

        this.user = this.userServices.getUser(principal.getName());
        this.loanServices.saveLoan(loan, user);

        return ResponseEntity.ok(loan);
    }

    @DeleteMapping("/delete/loan")
    public String deleteLoan(
            @RequestParam("id") int id,
            Principal principal,
            Session session) {

       this.userServices.deleteRes(id, this.userServices.getUser(principal.getName()));

        return "redirect:/user/myRes";

    }
    
}
