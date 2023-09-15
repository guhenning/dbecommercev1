package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the Item with Id: {0}", id));
    }
}
