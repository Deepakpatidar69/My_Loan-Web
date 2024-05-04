package com.web.my.loan.app.services;

import com.web.my.loan.app.entity.ApprovedLoan;
import com.web.my.loan.app.entity.BorrowedLoan;
import com.web.my.loan.app.entity.Loan;
import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.repo.ApprovedLoanRepo;
import com.web.my.loan.app.repo.BorrowedLoanRepo;
import com.web.my.loan.app.repo.LoanRepo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServices {

    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private BorrowedLoanRepo borrowedLoanRepo;

    @Autowired
    private ApprovedLoanRepo approvedLoanRepo;

    private String message;
    private String type;

    @Autowired
    private DateTimeFormatter formatter; // Change the pattern as needed

    public Message saveLoan(Loan loanData, User user) {

        try {

            loanData.setLoanApproval("No");
            loanData.setDate(LocalDate.now().format(formatter));
            // Setting Data in the BorrowedLoan And Save
            BorrowedLoan borrowedLoan = new BorrowedLoan();
            borrowedLoan.setEndUser(user);
            borrowedLoan.setLoanUser(loanData);
            // Save Data In Loan
            this.loanRepo.save(loanData);
            this.borrowedLoanRepo.save(borrowedLoan);

            message = "Loan Request Is SuccessFully Send ";
            type = "alert-success";
        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR please try again later!!";
            type = "alert-danger";
        }

        return new Message(message, type);
    }

    public String updateLoan(Loan loan) {
        this.loanRepo.save(loan);

        return "loan Approved Successfully";
    }

    public Loan getLoanById(int id) {

        return this.loanRepo.findById(id).get();
    }

    public BorrowedLoan getBloanByLoan(Loan loan, User user) {

        return this.borrowedLoanRepo.findByLoanUserAndEndUser(loan, user);
    }

    public List<Loan> getloan() {

        return this.loanRepo.findAll();
    }

    public String deleteLoanRes(int index, User user) {

        BorrowedLoan Bloan = user.getBorrowList().get(index);
        Loan loan = Bloan.getLoanUser();

        if (loan.getLoanApproval().equals("Yes")) {
            return "this loan request is approved by other user you can not delete this!!";
        }

        user.getBorrowList().remove(Bloan);

        Bloan.setEndUser(null);
        Bloan.setLoanUser(null);

        this.loanRepo.delete(loan);
        this.borrowedLoanRepo.delete(Bloan);

        return "Loan Request Delete Successfully";
    }

    // Find Approve User Detail
    public User getdetailofUser(Loan loan) {

        ApprovedLoan approvedLoan = approvedLoanRepo.findByLoanUser(loan);
        User user = approvedLoan.getEndUser();

        return user;
    }

}
