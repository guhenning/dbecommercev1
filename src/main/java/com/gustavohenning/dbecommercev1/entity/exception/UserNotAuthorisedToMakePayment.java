package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class UserNotAuthorisedToMakePayment extends RuntimeException{

    public UserNotAuthorisedToMakePayment (final String username, final Long cartId ) {
        super(MessageFormat.format("The username: {0} is not authorised to make the payment for CartId: {1}", username, cartId));
    }
}