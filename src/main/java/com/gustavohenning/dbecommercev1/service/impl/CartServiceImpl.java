package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.exception.CartNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public Cart addCart(Cart cart, ApplicationUser user) {
        cart.setUserId(user.getUserId()); // Associate the cart with user
        user.setCart(cart);
        return cartRepository.save(cart);
    }

    public List<Cart> getCarts() {
        return new ArrayList<>(cartRepository.findAll());
    }

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow(() ->
                new CartNotFoundException(id));
    }

    public Cart deleteCart(Long id) {
        Cart cart = getCart(id);
        cartRepository.delete(cart);
        return cart;
    }

    @Transactional
    public Cart editCart(Long id, Cart cart) {
        Cart cartToEdit = getCart(id);
        cartToEdit.setUserId(cart.getUserId());
        return cartToEdit;
    }

}
