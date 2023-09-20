package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Category;

import java.util.List;

public interface BrandService {

    Brand addBrand(Brand brand);

    List<Brand> getBrands();

    Iterable<Brand> findByNameContainingIgnoreCase(String name);

    Brand getBrand(Long id);

    Brand deleteBrand(Long id);

    Brand editBrand(Long id, Brand brand);

}
