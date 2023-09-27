package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.CartItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CartItem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int itemQuantity;

    @ManyToOne
    private Cart cart;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static CartItem from(CartItemDTO cartItemDto) {
        CartItem cartItem = new CartItem();
        cartItem.setCreatedDate(cartItemDto.getCreatedDate());
        cartItem.setUpdatedDate(cartItemDto.getUpdatedDate());

        Item item = new Item();
        item.setId(cartItemDto.getItemId());
        cartItem.setItemQuantity(cartItemDto.getItemQuantity());
        cartItem.setItem(item);


        return cartItem;
    }
}