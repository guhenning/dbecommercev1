package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDTO;

public interface CartItemService {

    CartItem getCartItem(Long id);

    Cart addCartItemToCart(Long cartId, CartItemDTO cartItemDto);

    Cart removeCartItemFromCart(Long cartId, Long cartItemId);

    void removeCartItemsAndDeleteFromCart(Long cartId);

}