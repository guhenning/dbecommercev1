package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Cart;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {

    private Long id;

    private CartWithUserDto cartWithUserDto;

    private double totalPrice;
    private double discountedPrice;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static CartDto from(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());

        CartWithUserDto cartWithUserDto = new CartWithUserDto();
        cartWithUserDto.setCartId(cart.getId());
        cartWithUserDto.setUserId(cart.getUserId().getUserId());

        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());

        cartWithUserDto.setCartItems(cartItemDtos);
        cartDto.setCartWithUserDto(cartWithUserDto);

        // Calculate total prices
        double totalSalePrice = cartItemDtos.stream()
                .mapToDouble(CartItemDto::getItemTotalPrice)
                .sum();

        double totalDiscountedPrice = cartItemDtos.stream()
                .mapToDouble(CartItemDto::getItemDiscountedPrice)
                .sum();

        // Format totalSalePrice and totalDiscountedPrice to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double formattedTotalPrice = Double.parseDouble(decimalFormat.format(totalSalePrice));
        double formattedDiscountedPrice = Double.parseDouble(decimalFormat.format(totalDiscountedPrice));

        cartDto.setTotalPrice(formattedTotalPrice);
        cartDto.setDiscountedPrice(formattedDiscountedPrice);

        cartDto.setCreatedDate(cart.getCreatedDate());
        cartDto.setUpdatedDate(cart.getUpdatedDate());

        return cartDto;
    }
}