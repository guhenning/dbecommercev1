package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDto;

import java.util.List;

public interface CartItemService {

    CartItem addCartItem(CartItem cartItem);

    List<CartItem> getCartItems();

    CartItem getCartItem(Long id);

    Cart addCartItemToCart(Long cartId, CartItemDto cartItemDto);

    Cart removeCartItemFromCart(Long cartId, Long cartItemId);

}