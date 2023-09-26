package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class UserNotAuthorisedToModifyCart extends RuntimeException{

    public UserNotAuthorisedToModifyCart(final String username, final Long cartId ) {
        super(MessageFormat.format("The username: {0} is not authorised to modify the CartId: {1}", username, cartId));
    }
}