package com.gustavohenning.dbecommercev1.entity.dto;


import com.gustavohenning.dbecommercev1.entity.Cart;
import lombok.Data;

@Data
public class PlainCartDTO {

    private Long id;
    private Long userId;

    public static PlainCartDTO from(Cart cart) {
        PlainCartDTO plainCartDto = new PlainCartDTO();
        plainCartDto.setId(cart.getId());
        plainCartDto.setUserId(cart.getUser().getUserId());
        return plainCartDto;
    }
}