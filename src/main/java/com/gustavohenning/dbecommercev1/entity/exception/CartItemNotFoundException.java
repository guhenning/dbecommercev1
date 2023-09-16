package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the CartItem with Id: {0}", id));
    }
}
