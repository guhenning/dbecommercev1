package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.CartItem;
import com.gustavohenning.dbecommercev1.entity.dto.CartDTO;
import com.gustavohenning.dbecommercev1.entity.dto.CartItemDTO;
import com.gustavohenning.dbecommercev1.service.CartItemService;
import com.gustavohenning.dbecommercev1.service.impl.ItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/{cartId}/cartItem")
@CrossOrigin("*")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService, ItemServiceImpl itemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "Get CartItem By ID", description = "Get the Cart Item from ID")
    //Todo fix all carts getting this itemId
    @GetMapping(value = "{id}")
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable final Long id) {
        CartItem cartItem = cartItemService.getCartItem(id);
        return new ResponseEntity<>(CartItemDTO.from(cartItem), HttpStatus.OK);
    }

    @Operation(summary = "Add Item to Cart using Cart ID", description = "Add Item to CartItem")
    @PostMapping(value = "/add")
    public ResponseEntity<CartDTO> addCartItemToCart(@PathVariable final Long cartId, @RequestBody final CartItemDTO cartItemDto,  @RequestHeader("Authorization") String token) {
        Cart cart = cartItemService.addCartItemToCart(cartId, cartItemDto, token);
        return new ResponseEntity<>(CartDTO.from(cart), HttpStatus.OK);
    }

    @Operation(summary = "Delete Item from Cart ", description = "Delete Item from CartItem")
    @DeleteMapping(value = "/delete/{cartItemId}")
    public ResponseEntity<CartDTO> deleteItemFromCart(@PathVariable final Long cartId, @PathVariable final Long cartItemId, @RequestHeader("Authorization") String token) {
        Cart cart = cartItemService.removeCartItemFromCart(cartId, cartItemId, token);
        return new ResponseEntity<>(CartDTO.from(cart), HttpStatus.OK);
    }
}
