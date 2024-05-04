/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.web.my.loan.app.config;

import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Others {
    
    
    @Bean
    public DateTimeFormatter dateTimeFormatter(){
        
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    
}
