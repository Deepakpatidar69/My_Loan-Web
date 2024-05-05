package com.web.my.loan.app.helper;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class Helper {
    
    static int otp;
    
    public static int otpGenerator() {
        
        Random random = new Random();
        
        otp = random.nextInt(111111, 899999);
        
        return otp;
    }
    
    public boolean isMatch(int fillotp) {
        return fillotp == otp;
    }
    
    public void removeSession() {
        
        try {
            
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest()
                    .getSession();
            
            session.removeAttribute("msg");
            session.removeAttribute("msg1");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String dateConvert(String date , int duration){
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

         String Cdate = null;
	        try {
	            Cdate = LocalDate.parse(date, formatter).plusMonths(duration).format(formatter);
	        } catch (DateTimeParseException e) {
                }
                
                return Cdate;
    }
    
}
