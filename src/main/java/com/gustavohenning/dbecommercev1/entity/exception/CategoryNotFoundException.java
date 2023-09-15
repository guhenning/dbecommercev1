package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(final Long id) {
        super(MessageFormat.format("Could not found the Category with Id: {0}", id));
    }
}
