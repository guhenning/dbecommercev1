package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDTO;
import com.gustavohenning.dbecommercev1.entity.exception.CartItemNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.UserNotAuthorisedToSeeOrModifyCart;
import com.gustavohenning.dbecommercev1.repository.CartItemRepository;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.service.CartItemService;
import com.gustavohenning.dbecommercev1.util.CartOwner;
import com.gustavohenning.dbecommercev1.util.ExtractUserFromToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartServiceImpl cartService;
    private final ItemServiceImpl itemService;
    private final CartRepository cartRepository;
    private final CartOwner cartOwner;
    private final ExtractUserFromToken extractUserFromToken;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartServiceImpl cartService, ItemServiceImpl itemService, CartRepository cartRepository, CartOwner cartOwner, ExtractUserFromToken extractUserFromToken) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.itemService = itemService;
        this.cartRepository = cartRepository;
        this.cartOwner = cartOwner;
        this.extractUserFromToken = extractUserFromToken;
    }

    public CartItem getCartItem(Long id) {
        return cartItemRepository.findById(id).orElseThrow( () -> new CartItemNotFoundException(id));
    }

    @Transactional
    public Cart addCartItemToCart(Long cartId, CartItemDTO cartItemDto, @RequestHeader("Authorization") String token) {
        String userIdentifier = extractUserFromToken.extractUserIdentifier(token);

        if (cartOwner.isCartOwner(cartId, userIdentifier)) {
            Cart cart = cartService.getCart(cartId);
            CartItem cartItem = CartItem.from(cartItemDto);
            cartItem.setCart(cart);

            Item item = itemService.getItem(cartItemDto.getItemId());

            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(items -> items.getItem().getId().equals(cartItemDto.getItemId()))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                existingCartItem.get().setItemQuantity(existingCartItem.get().getItemQuantity() + 1);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setItem(item);
                newCartItem.setItemQuantity(1);
                newCartItem.setCart(cart);
                cart.getCartItems().add(newCartItem);
            }

            return cartRepository.save(cart);
        } else throw new UserNotAuthorisedToSeeOrModifyCart(userIdentifier, cartId);
    }

    @Transactional
    public Cart removeCartItemFromCart(Long cartId, Long cartItemId, @RequestHeader("Authorization") String token) {
        String userIdentifier = extractUserFromToken.extractUserIdentifier(token);

        Cart cart = cartService.getCart(cartId);

        if (cartOwner.isCartOwner(cartId, userIdentifier)) {

            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(items -> items.getItem().getId().equals(cartItemId))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                if (existingCartItem.get().getItemQuantity() > 1) {
                    existingCartItem.get().setItemQuantity(existingCartItem.get().getItemQuantity() - 1);
                } else {
                    cart.removeCartItem(existingCartItem.get());
                    cartItemRepository.delete(existingCartItem.get());
                }
            } else throw new CartItemNotFoundException(cartItemId);

            return cartRepository.save(cart);

        } else throw new UserNotAuthorisedToSeeOrModifyCart(userIdentifier, cartId);
    }

    @Transactional
    public void removeCartItemsAndDeleteFromCartAfterPayment(Long cartId) {
        Cart cart = cartService.getCart(cartId);

        List<CartItem> cartItemsToRemove = new ArrayList<>(cart.getCartItems());

        for (CartItem cartItem : cartItemsToRemove) {

            Item item = cartItem.getItem();
            int cartItemQuantity = cartItem.getItemQuantity();
            item.setStockQuantity(item.getStockQuantity() - cartItemQuantity);

            cart.removeCartItem(cartItem);
        }

        for (CartItem cartItem : cartItemsToRemove) {
            cartItemRepository.delete(cartItem);
        }
    }
}