package com.example.demo.controller;

import com.example.demo.dto.UserDetailsDto;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/getUserDetails/{cardNumber}")
    public String getDetails(@PathVariable String cardNumber, HttpServletResponse httpServletResponse){
        String aa = "";
        try {
            UserDetailsDto userInfo = demoService.getUserCardInfo(cardNumber);
            if (!userInfo.getUserName().isEmpty()) {
                Cookie cookie = new Cookie("userId", String.valueOf(userInfo.getId()));
                cookie.setHttpOnly(true);
                cookie.setPath("/api");
                cookie.setSecure(true);
                cookie.setMaxAge(600);
                Cookie cookie1 = new Cookie("cardNumber", cardNumber);
                cookie1.setHttpOnly(true);
                cookie1.setPath("/api");
                cookie1.setSecure(true);
                cookie1.setMaxAge(600);
                httpServletResponse.addCookie(cookie);
                httpServletResponse.addCookie(cookie1);
                aa="Welcome "+userInfo.getUserName()
                        +"<br> 1. Withdrawal <br> 2.PIN Reset <br> 3. Check Balance <br> 4. Cancel <br> <b>Select any one of the option<b>";
            } else {
                aa = "User Details is not available";
            }
        }catch (Exception e){
            e.printStackTrace();
            aa = "Server Error Occurred. Please try again later.";
        }
        return aa;
    }

    @GetMapping("/getATMOptions/{atmOption}/{cardPIN}")
    public String checkPIN(@PathVariable int atmOption, @PathVariable int cardPIN, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try {
            Cookie[] cookies = httpServletRequest.getCookies();
            if(cookies != null) {
                return demoService.checkPIN(atmOption, cardPIN, cookies, httpServletResponse);
            }else{
                return "Session Expired. Please enter the card number again.";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Server Error Occurred. Please try again later.";
        }
    }

    @GetMapping("/getAmount/{amount}")
    public String withdrawalResult(@PathVariable int amount, HttpServletRequest httpServletRequest){
        try {
            Cookie[] cookies = httpServletRequest.getCookies();
            if(cookies != null) {
                return demoService.withdrawalResult(amount);
            }
            else{
                return "Session Expired. Please enter the card number again.";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Server Error Occurred. Please try again later.";
        }
    }
}
