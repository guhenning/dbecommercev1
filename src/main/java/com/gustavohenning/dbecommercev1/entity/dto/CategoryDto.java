package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Category;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static CategoryDto from(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setCreatedDate(category.getCreatedDate());
        categoryDto.setUpdatedDate(category.getUpdatedDate());

        return categoryDto;
    }
}
