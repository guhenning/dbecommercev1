package com.gustavohenning.dbecommercev1.util;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.repository.UserRepository;
import com.gustavohenning.dbecommercev1.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CartOwner {

    private final UserRepository userRepository;
    private final CartServiceImpl cartService;

    @Autowired
    public CartOwner(UserRepository userRepository, CartServiceImpl cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public boolean isCartOwner(Long cartId, String username) {
        Cart cart = cartService.getCart(cartId);
        Optional<ApplicationUser> userOptional = userRepository.findByUsername(username);

        return userOptional.isPresent() && cart.getUser().equals(userOptional.get());
    }
}
