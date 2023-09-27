package com.gustavohenning.dbecommercev1.entity.exception;

import java.text.MessageFormat;

public class ItemStockCannotBeNegative extends RuntimeException {

    public ItemStockCannotBeNegative(Long id, int previousStock, int newStock) {
        super(MessageFormat.format("Item with ID {0} cannot have negative stock. Previous Stock: {1}, New Stock: {2}", id, previousStock, newStock));
    }
}