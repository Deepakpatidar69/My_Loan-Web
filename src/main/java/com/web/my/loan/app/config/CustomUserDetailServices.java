package com.web.my.loan.app.config;

import com.web.my.loan.app.entity.User;
import com.web.my.loan.app.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailServices implements UserDetailsService {

    @Autowired
    UserServices userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("could Not found user !!");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

              return customUserDetails;
    }

}
