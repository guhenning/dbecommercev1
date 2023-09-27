package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.CartDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.MERGE,
            mappedBy = "cart")
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public static Cart from(CartDTO cartDto) {
        Cart cart = new Cart();
        cart.setUserId(cartDto.getUserId());
        cart.setCreatedDate(cartDto.getCreatedDate());
        cart.setUpdatedDate(cartDto.getUpdatedDate());

        return cart;
    }
}
