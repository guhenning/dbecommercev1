package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class UsernameAlreadyExistsException extends RuntimeException{

    public UsernameAlreadyExistsException(final String username) {
        super(MessageFormat.format("An account with this username already exists. Username: {0}", username));
    }
}
