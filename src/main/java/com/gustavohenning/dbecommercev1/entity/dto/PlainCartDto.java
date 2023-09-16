package com.gustavohenning.dbecommercev1.entity.dto;


import com.gustavohenning.dbecommercev1.entity.Cart;
import lombok.Data;

@Data
public class PlainCartDto {

    private Long id;
    private Long userId;

    public static PlainCartDto from(Cart cart) {
        PlainCartDto plainCartDto = new PlainCartDto();
        plainCartDto.setId(cart.getId());
        plainCartDto.setUserId(cart.getUserId());
        return plainCartDto;
    }
}