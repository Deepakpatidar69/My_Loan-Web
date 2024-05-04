package com.web.my.loan.app.repo;

import com.web.my.loan.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer>{
    
    public User findUserByUserName(String userName);
    
}