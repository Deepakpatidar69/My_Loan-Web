package com.web.my.loan.app.services;

import com.web.my.loan.app.entity.Address;
import com.web.my.loan.app.entity.ApprovedLoan;
import com.web.my.loan.app.entity.BorrowedLoan;
import com.web.my.loan.app.entity.Loan;
import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.handlers.ErrorHandlers;
import com.web.my.loan.app.helper.Helper;
import com.web.my.loan.app.helper.Message;
import com.web.my.loan.app.repo.AddressRepo;
import com.web.my.loan.app.repo.ApprovedLoanRepo;
import com.web.my.loan.app.repo.UserRepository;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ApprovedLoanRepo approveLoanRepo;

    @Autowired
    private LoanServices loanService;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private MailServices mailService;

    @Autowired
    private Helper helper;

    private User user;

    String message;
    String type;

    // Set The Format of Date
    @Autowired
    DateTimeFormatter formatter; // Change the pattern as needed

    // Save User Data in DataBase if The Otp is Match
    public Message saveUser(User user, int otp) {

        if (helper.isMatch(otp)) {

            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setUserRole("ROLE_USER");
                user.setImage(user.getImg().getBytes());
                this.userRepo.save(user);
                message = "Signup Successfully Completed";
                type = "alert-success";

                return new Message(message, type);

            } catch (Exception e) {

                return ErrorHandlers.ispresentorNot(e, user);
            }
            
        } else {
            message = "Otp Doesnt Match";
            type = "alert-danger";
        }

        return new Message(message, type);
    }

    public boolean updateUser(User user) {

        try {

            this.userRepo.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get The User By UserName
    public User getUser(String userName) {

        return this.userRepo.findUserByUserName(userName);
    }

    // Add Address
    public Message addAddress(Address address, User user) {

        try {
            address.setUser(user);
            user.getAddress().add(address);
            this.userRepo.save(user);

            message = "SuccessFully Saved Address";
            type = "alert-success";

        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR please try again later ";
            type = "alert-danger";
        }
        return new Message(message, type);
    }

    // Give to The Address Which Are Update
    public Address getUpdateAddress(User user, int index) {

        List<Address> adds = user.getAddress();

        Address address = adds.get(index);
        return address;
    }

    // Update Address Detail
    public Message updateAddress(Address address, User user) {

        try {
            addAddress(address, user);
            message = " Address SuccessFully Updated";
            type = "alert-success";
        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR please try again later";
            type = "alert-danger";
        }

        return new Message(message, type);
    }

    // Remove Address
    public Message removeAddress(User user, int index) {

        try {
            Address address = user.getAddress().remove(index);
            address.setUser(null);
            addressRepo.delete(address);
            message = "Address Remove Successfully";
            type = "alert-warning";
        } catch (Exception e) {

            message = "Address Remove Successfully";
            type = "alert-danger";

        }
        return new Message(message, type);
    }

    // Updatev Profile Data 
    public Message updateProfile(User updateuser, User oldUser) {

        oldUser.setFirstName(updateuser.getFirstName());
        oldUser.setLastName(updateuser.getLastName());
        oldUser.setMobileNumber(updateuser.getMobileNumber());
        oldUser.setGender(updateuser.getGender());

        try {
            this.userRepo.save(oldUser);
            message = "Profile Update SuccessFully";
            type = "alert-success";
        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR try again later";
            type = "alert-danger";
        }

        return new Message(message, type);
    }

    // Delete Loan Request
    public Message deleteRes(int id, User user) {

        try {

            message = loanService.deleteLoanRes(id, user);
            type = "alert-warning";

        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR please try again later";
            type = "alert-danger";
        }
        return new Message(message, type);
    }

    // Approved The Loan Request
    public Message approveRes(int id, Principal principal) {

        try {
            // get Loan And User or Check the Loan request and Approve request are not a same user
            user = this.userRepo.findUserByUserName(principal.getName());
            Loan loan = this.loanService.getLoanById(id);
            loan.setLoanApproval("Yes");

            // Set Approved Date
            loan.setDate(LocalDate.now().format(formatter));
            BorrowedLoan bloan = this.loanService.getBloanByLoan(loan, user);

            if (bloan == null) {

                // It ensure that the loan user or approve user are different
                ApprovedLoan Aloan = new ApprovedLoan();
                Aloan.setLoanUser(loan);
                Aloan.setEndUser(user);

                this.approveLoanRepo.save(Aloan);
                this.loanService.updateLoan(loan);
                message = "Request Successfully Approved ";
                type = "alert-success";

                return new Message(message, type);
            }
            message = "You can Not Approve your own request ";
            type = "alert-warning";
        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR please try again later ";
            type = "alert-danger";

        }
        return new Message(message, type);
    }

    // Change Password
    public Message changePassword(String currentPass, String newPass, User user) {

        type = "alert-danger";
        if (currentPass.equals(newPass)) {
            message = "Both Password Same";
            return new Message(message, type);
        }

        try {

            if (passwordEncoder.matches(currentPass, user.getPassword())) {

                user.setPassword(passwordEncoder.encode(newPass));
                this.userRepo.save(user);
                message = "Password Change SuccessFully";
                type = "alert-success";
            } else {
                message = "Current Password Are Not Match!!";
            }

        } catch (Exception e) {
            message = "INTERNAL_SERVER_ERROR try again later";
        }

        return new Message(message, type);
    }

    // Verify Email And Send Otp for Change Email
    public Message VerifyEmail(String currentEmail, String newEmail, String userName , String regarding) {

        type = "alert-danger";
        if (!currentEmail.equals(userName)) {
            message = "Current Email is not match!!";
            return new Message(message, type);

        }

        if (currentEmail.equals(newEmail)) {
            message = "Both Email Are Same";
            return new Message(message, type);

        }

        if (getUser(newEmail) != null) {
            message = "Email is already exist!!";
            return new Message(message, type);
            
        }

        try {

            Message msg = this.mailService.sendOtp(newEmail , "email");
            return msg;

        } catch (MessagingException e) {

            message = "INTERNAL_SERVER_ERROR try agian later";
        }
        return new Message(message, type);
    }

    // Verify Otp And Password for Change Email
    public Message verifyEmailPass(String email, int otp, String password, User user) {

        type = "alert-danger";
        if (!helper.isMatch(otp)) {

            message = "Otp Doesn't Match";
            return new Message(message, type);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            message = "Password Doesn't Match";
        } else {
            
            user.setUserName(email);
            this.userRepo.save(user);
            message = "Email Change Successfully";
            type = "alert-success";
        }
        return new Message(message, type);
    }

}
