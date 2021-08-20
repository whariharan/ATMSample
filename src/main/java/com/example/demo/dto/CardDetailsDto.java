package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDetailsDto {
    private int id;
    private int userId;
    private String cardNo;
    private int cardPin;
    private LocalDate cardExpiryDate;
    private long cardBalance;
}
