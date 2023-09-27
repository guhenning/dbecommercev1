package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.ApplicationUser;
import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.exception.CartNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.UserNotAuthorisedToSeeOrModifyCart;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.service.CartService;
import com.gustavohenning.dbecommercev1.util.CartOwner;
import com.gustavohenning.dbecommercev1.util.ExtractUserFromToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;

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

    public Cart getUserCart(Long id, @RequestHeader("Authorization") String token) {
        //TODO use jwt to get cart
       // String userIdentifier = extractUserFromToken.extractUserIdentifier(token);

       // if (cartOwner.isCartOwner(id, userIdentifier)) {
            return cartRepository.findById(id).orElseThrow(() ->
                    new CartNotFoundException(id));
//} else throw new UserNotAuthorisedToSeeOrModifyCart(userIdentifier, id);
    }
}
