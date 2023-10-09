package com.gustavohenning.dbecommercev1.entity.dto;

import com.gustavohenning.dbecommercev1.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static CategoryDTO from(Category category) {
        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setCreatedDate(category.getCreatedDate());
        categoryDto.setUpdatedDate(category.getUpdatedDate());

        return categoryDto;
    }
}
