package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.exception.BrandNotFoundException;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
import com.gustavohenning.dbecommercev1.service.BrandService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> getBrands() {
        return new ArrayList<>(brandRepository.findAll());
    }

    public Brand getBrand(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));
    }

    public Brand deleteBrand(Long id) {
        Brand brand = getBrand(id);
        brandRepository.delete(brand);
        return brand;
    }

    @Transactional
    public Brand editBrand(Long id, Brand brand) {
        Brand brandToEdit = getBrand(id);

        if (brand.getName() != null) {
            brandToEdit.setName(brand.getName());
        }

        LocalDateTime currentUpdateDate = brandToEdit.getUpdatedDate();
        if (currentUpdateDate == null) {
            brandToEdit.setUpdatedDate(brandToEdit.getCreatedDate());
        } else {
            brandToEdit.setUpdatedDate(brand.getUpdatedDate());
        }


        return brandToEdit;
    }
}
