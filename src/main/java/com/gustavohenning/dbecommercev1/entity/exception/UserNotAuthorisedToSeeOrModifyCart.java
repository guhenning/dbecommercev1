package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class UserNotAuthorisedToSeeOrModifyCart extends RuntimeException{

    public UserNotAuthorisedToSeeOrModifyCart(final String username, final Long cartId ) {
        super(MessageFormat.format("The username: {0} is not authorised to see or modify the CartId: {1}", username, cartId));
    }
}