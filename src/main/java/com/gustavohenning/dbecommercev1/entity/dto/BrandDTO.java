package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Brand;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class BrandDTO {
    private Long id;
    private String name;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static BrandDTO from(Brand brand) {
        BrandDTO brandDto = new BrandDTO();
        brandDto.setId(brand.getId());
        brandDto.setName(brand.getName());
        brandDto.setCreatedDate(brand.getCreatedDate());
        brandDto.setUpdatedDate(brand.getUpdatedDate());

        return brandDto;
    }
}
