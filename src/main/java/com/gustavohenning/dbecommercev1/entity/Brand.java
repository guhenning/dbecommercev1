package com.gustavohenning.dbecommercev1.entity;

import com.gustavohenning.dbecommercev1.entity.dto.BrandDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;



import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;


    public static Brand from(BrandDTO brandDto) {
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand.setCreatedDate(brandDto.getCreatedDate());
        brand.setUpdatedDate(brandDto.getUpdatedDate());
        return brand;

    }


}
