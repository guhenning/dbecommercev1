package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.dto.CartDto;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDto;
import com.gustavohenning.dbecommercev1.service.CartItemService;
import com.gustavohenning.dbecommercev1.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/{cartId}/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService, ItemServiceImpl itemService) {
        this.cartItemService = cartItemService;
    }

    //@Operation(summary = "Get CartItem By ID", description = "Get the Cart Item from ID")
    @GetMapping(value = "{id}")
    public ResponseEntity<CartItemDto> getCartItem(@PathVariable final Long id) {
        CartItem cartItem = cartItemService.getCartItem(id);
        return new ResponseEntity<>(CartItemDto.from(cartItem), HttpStatus.OK);
    }

    //@Operation(summary = "Add Item to Cart using Cart ID", description = "Add Item to CartItem")
    @PostMapping(value = "/add")
    public ResponseEntity<CartDto> addCartItemToCart(@PathVariable final Long cartId, @RequestBody final CartItemDto cartItemDto) {
        Cart cart = cartItemService.addCartItemToCart(cartId, cartItemDto);
        return new ResponseEntity<>(CartDto.from(cart), HttpStatus.OK);
    }

    //@Operation(summary = "Delete Item from Cart ", description = "Delete Item from CartItem")
    @DeleteMapping(value = "/delete/{cartItemId}")
    public ResponseEntity<CartDto> deleteItemFromCart(@PathVariable final Long cartId, @PathVariable final Long cartItemId) {
        Cart cart = cartItemService.removeCartItemFromCart(cartId, cartItemId);
        return new ResponseEntity<>(CartDto.from(cart), HttpStatus.OK);
    }
}
