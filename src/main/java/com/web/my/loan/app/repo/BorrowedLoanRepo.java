/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.web.my.loan.app.repo;

import com.web.my.loan.app.entity.BorrowedLoan;
import com.web.my.loan.app.entity.Loan;
import com.web.my.loan.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BorrowedLoanRepo extends JpaRepository<BorrowedLoan, Integer>{
    
    public BorrowedLoan findByLoanUserAndEndUser(Loan loan , User user);
}
