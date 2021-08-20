package com.example.demo.service;

import com.example.demo.dto.CardDetailsDto;
import com.example.demo.dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;


@Service
public class DemoService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDetailsDto getUserCardInfo(String cardNumber){
         return jdbcTemplate.query("select u.id, u.user_name, cd.card_number " +
                 "from USERS u LEFT JOIN CARD_DETAILS cd ON cd.user_id = u.id Where cd.card_number = '"+cardNumber+"' "
                 , (ResultSet rs) -> {
             UserDetailsDto userDetailsDto = new UserDetailsDto();
             CardDetailsDto cardDetailsDto = new CardDetailsDto();
             if(rs.next()) {
                 userDetailsDto.setId(rs.getInt("id"));
                 userDetailsDto.setUserName(rs.getString("user_name"));
                 cardDetailsDto.setCardNo(rs.getString("card_number"));
                 userDetailsDto.setCardDetailsDto(cardDetailsDto);
             }
             return userDetailsDto;
         });
     }

     private String selectedOption(int option){
         String result = "";
         switch (option) {
             case 1:
                 result = "Enter Denominations in  $50, $20, $10";
                 break;
             case 2:
                 result = "Pin Reset option is coming soon";
                 break;
             case 3:
                 result = "Check Account Balance option is coming soon";
                 break;
             case 4:
                 result = "Thank You. Visit Again";
                 break;
             default:
                 result = "Invalid Option";
                 break;
         }
         return result;
     }

     public String withdrawalResult(int enteredAmount){
         StringBuilder msgBuild = new StringBuilder();
         Integer[] availableDenominations = {50,20,10};
         if(enteredAmount % 10 == 0) {
             int count = 0;
             for (int i = 0; i < availableDenominations.length; i++) {
                 if (enteredAmount > availableDenominations[i]) {
                     int value = enteredAmount - (availableDenominations[i] * (enteredAmount / availableDenominations[i]));
                     msgBuild.append(" "+ availableDenominations[i] + "*" + enteredAmount / availableDenominations[i]);
                     if (value == 0) {
                         break;
                     } else {
                         for (int j = i + 1; j < availableDenominations.length; j++) {
                             if (value % availableDenominations[j] == 0) {
                                 msgBuild.append(" "+availableDenominations[j] + "*" + value / availableDenominations[j]);
                                 count++;
                                 break;
                             } else if (value > availableDenominations[j]) {
                                 msgBuild.append(" "+availableDenominations[j] + "*" + value / availableDenominations[j]);
                                 value = value - (availableDenominations[j] * (value / availableDenominations[j]));
                             }
                         }
                     }
                     if(count > 0){
                         break;
                     }
                 }
             }
         }
         return msgBuild.length() > 0 ? "Please collect the cash : "+msgBuild.toString() : "Amount cannot be dispensed";
     }

    public String checkPIN(int atmOption, int cardPIN, Cookie[] cookies, HttpServletResponse httpServletResponse) {
        if(atmOption != 4) {
            return jdbcTemplate.query("select card_number " +
                            "from CARD_DETAILS Where card_number = '" + cookies[1].getValue() + "' " +
                            "and  user_id = " + cookies[0].getValue() + " " +
                            "and  card_pin = '" + cardPIN + "'"
                    , (ResultSet rs) -> {
                        if (rs.next()) {
                            return selectedOption(atmOption);
                        } else {
                            return "PIN is incorrect";
                        }

                    });
        }else{
            Cookie cookie = new Cookie("userId", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge(0);
            Cookie cookie1 = new Cookie("cardNumber", null);
            cookie1.setHttpOnly(true);
            cookie1.setPath("/");
            cookie1.setSecure(true);
            cookie1.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
            httpServletResponse.addCookie(cookie1);
            return "Thank you. Visit Again";
        }
    }
}
