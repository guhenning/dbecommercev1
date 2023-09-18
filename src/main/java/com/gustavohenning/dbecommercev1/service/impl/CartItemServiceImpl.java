package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDto;
import com.gustavohenning.dbecommercev1.entity.exception.CartItemNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CartItemRepository;
import com.gustavohenning.dbecommercev1.repository.CartRepository;
import com.gustavohenning.dbecommercev1.service.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartServiceImpl cartService;
    private final ItemServiceImpl itemService;
    private final CartRepository cartRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartServiceImpl cartService, ItemServiceImpl itemService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.itemService = itemService;
        this.cartRepository = cartRepository;
    }

    //TODO check if necessary
    public CartItem addCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItemRepository.findAll());
    }

    public CartItem getCartItem(Long id) {
        return cartItemRepository.findById(id).orElseThrow( () -> new CartItemNotFoundException(id));
    }

    @Transactional
    public Cart addCartItemToCart(Long cartId, CartItemDto cartItemDto) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = CartItem.from(cartItemDto);
        cartItem.setCart(cart);

        Item item = itemService.getItem(cartItemDto.getItemId());

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(items -> items.getItem().getId().equals(cartItemDto.getItemId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.get().setItemQuantity(existingCartItem.get().getItemQuantity() + 1);

            // TODO setStockQuantity only after payment
            //item.setStockQuantity(item.getStockQuantity() - 1);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setItem(item);
            newCartItem.setItemQuantity(1);
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
           // TODO setStockQuantity only after payment
           // item.setStockQuantity(item.getStockQuantity() - 1);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeCartItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartService.getCart(cartId);


        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(items -> items.getItem().getId().equals(cartItemId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            if (existingCartItem.get().getItemQuantity() > 1) {
                existingCartItem.get().setItemQuantity(existingCartItem.get().getItemQuantity() - 1);
            } else {
                cart.removeCartItem(existingCartItem.get());
            }
        } else throw new CartItemNotFoundException(cartItemId);

        return cartRepository.save(cart);
    }
}