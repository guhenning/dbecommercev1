package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Cart;
import com.gustavohenning.dbecommercev1.entity.dto.CartDTO;
import com.gustavohenning.dbecommercev1.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get All Carts", description = "Get All Carts in DB")
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<Cart> carts = cartService.getCarts();
        List<CartDTO> cartsDto = carts.stream().map(CartDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(cartsDto, HttpStatus.OK);
    }
    @Operation(summary = "Get Cart By ID", description = "Get Cart from ID in DB")
    @GetMapping(value = "{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable final Long id) {
        Cart cart = cartService.getCart(id);
        return new ResponseEntity<>(CartDTO.from(cart) , HttpStatus.OK);
    }


}
