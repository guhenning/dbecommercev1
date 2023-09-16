package com.gustavohenning.dbecommercev1.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartWithUserDto {
    private Long cartId;
    private Long userId;
    private List<CartItemDto> cartItems;
}
