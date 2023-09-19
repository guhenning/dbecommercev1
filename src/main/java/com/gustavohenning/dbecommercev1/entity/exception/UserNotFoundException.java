package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the User with Id: {0}", id));
    }
}
