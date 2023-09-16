package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;

import java.util.List;

public interface CartService {

    //Cart addCart(Cart cart, ApplicationUser user);

    List<Cart> getCarts();

    Cart getCart(Long id);

    Cart deleteCart(Long id);

    Cart editCart(Long id, Cart cart);
}
