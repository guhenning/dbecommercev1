package com.gustavohenning.dbecommercev1.entity.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StripeChargeDTO {

    private String  stripeToken;
    private String  username;
    private double  amount;
    private boolean success;
    private String  message;
    private String chargeId;
    private Map<String,Object> additionalInfo = new HashMap<>();

}

