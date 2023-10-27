package com.gustavohenning.dbecommercev1.entity.dto;

import lombok.Data;

@Data
public class StripeTokenDTO {

    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;
    private String username;
    private boolean success;

}
