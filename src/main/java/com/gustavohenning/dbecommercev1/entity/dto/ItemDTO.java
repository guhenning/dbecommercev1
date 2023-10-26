package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.Item;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String imageUrl;
    private int stockQuantity;
    private double salePrice;
    private double costPrice;
    private double discount;

    private BrandDTO brand;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private List<Long> categoryIds;

    public static ItemDTO from(Item item) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setShortDescription(item.getShortDescription());
        itemDto.setLongDescription(item.getLongDescription());
        itemDto.setImageUrl(item.getImageUrl());
        itemDto.setStockQuantity(item.getStockQuantity());
        itemDto.setSalePrice(item.getSalePrice());
        itemDto.setCostPrice(item.getCostPrice());
        itemDto.setDiscount(item.getDiscount());
        itemDto.setCategoryIds(item.getCategories().stream().map(Category::getId).collect(Collectors.toList()));
        itemDto.setBrand(BrandDTO.from(item.getBrand()));
        itemDto.setCreatedDate(item.getCreatedDate());
        itemDto.setUpdatedDate(item.getUpdatedDate());

        return itemDto;


    }
}
