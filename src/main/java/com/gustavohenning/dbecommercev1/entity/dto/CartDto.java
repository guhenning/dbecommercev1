package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {

    private Long id;

    List<CartItemDto> cartItemsDto = new ArrayList<>();

    private double totalPrice;
    private double discountedPrice;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;


    private CartUserDto userId;



    public static CartDto from(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        CartUserDto userDto = new CartUserDto();
        userDto.setId(cart.getUserId().getUserId());
        cartDto.setUserId(userDto);
        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());
        cartDto.setCartItemsDto(cartItemDtos);

        cartDto.setCreatedDate(cart.getCreatedDate());
        cartDto.setUpdatedDate(cart.getUpdatedDate());



        double totalSalePrice = cartItemDtos.stream()
                .mapToDouble(cartItemDto -> cartItemDto.getItemSalePrice() * cartItemDto.getItemQuantity())
                .sum();

        double totalDiscountedPrice = cartItemDtos.stream()
                .mapToDouble(cartItemDto -> (cartItemDto.getItemSalePrice() - cartItemDto.getItemSalePrice() * cartItemDto.getItemDiscount()) * cartItemDto.getItemQuantity())
                .sum();


        // Format totalSalePrice to two decimal
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double formattedTotalPrice = Double.parseDouble(decimalFormat.format(totalSalePrice));
        double formattedDiscountedPrice = Double.parseDouble(decimalFormat.format(totalDiscountedPrice));

        cartDto.setTotalPrice(formattedTotalPrice);
        cartDto.setDiscountedPrice(formattedDiscountedPrice);

        return cartDto;
    }
}