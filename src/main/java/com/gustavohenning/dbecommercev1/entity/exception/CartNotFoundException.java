package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the Cart with Id: {0}", id));
    }
}