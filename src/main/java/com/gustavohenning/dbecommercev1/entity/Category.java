package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

   @ManyToMany(mappedBy = "categories")
    private List<Item> items;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public static Category from(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setCreatedDate(categoryDto.getCreatedDate());
        category.setUpdatedDate(categoryDto.getUpdatedDate());

        return category;
    }
}
