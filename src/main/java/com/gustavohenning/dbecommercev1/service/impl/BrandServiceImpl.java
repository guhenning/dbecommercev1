package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.BrandNotFoundException;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
import com.gustavohenning.dbecommercev1.service.BrandService;
import com.gustavohenning.dbecommercev1.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ItemService itemService;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, ItemService itemService) {
        this.itemService = itemService;
        this.brandRepository = brandRepository;
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> getBrands() {
        return new ArrayList<>(brandRepository.findAll());
    }

    public Iterable<Brand> findByNameContainingIgnoreCase(String name) {
        return brandRepository.findByNameContainingIgnoreCase(name);
    };

    public Brand getBrand(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));
    }

    public Brand deleteBrand(Long id) {
        Brand brand = getBrand(id);
        List<Item> itemsWithBrand = itemService.getItemsByBrandId(id);

        if (!itemsWithBrand.isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete a Brand that are associated with an or more Items," +
                    " please modify or delete the Items with this Brand");
        }
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
