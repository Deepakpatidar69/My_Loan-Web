/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.web.my.loan.app.repo;

import com.web.my.loan.app.entity.ApprovedLoan;
import com.web.my.loan.app.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApprovedLoanRepo extends JpaRepository<ApprovedLoan, Integer>{
 
    public ApprovedLoan findByLoanUser(Loan loan);
    
}
