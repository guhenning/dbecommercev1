package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class BrandNotFoundException extends RuntimeException {

    public BrandNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the Brand with Id: {0}", id));
    }

}
