package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.ItemDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String imageUrl;
    private int stockQuantity;
    private double salePrice;
    private double costPrice;
    private double discount;

    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_category", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "category_id")    )
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;






    public static Item from(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setShortDescription(itemDto.getShortDescription());
        item.setLongDescription(itemDto.getLongDescription());
        item.setImageUrl(itemDto.getImageUrl());
        item.setStockQuantity(itemDto.getStockQuantity());
        item.setSalePrice(itemDto.getSalePrice());
        item.setCostPrice(itemDto.getCostPrice());
        item.setDiscount(itemDto.getDiscount());
        item.setBrand(Brand.from(itemDto.getBrand()));
        item.setCreatedDate(itemDto.getCreatedDate());
        item.setUpdatedDate(itemDto.getUpdatedDate());
        return item;
    }


}
