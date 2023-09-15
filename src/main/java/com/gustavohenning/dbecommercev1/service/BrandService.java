package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Brand;

import java.util.List;

public interface BrandService {

    Brand addBrand(Brand brand);

    List<Brand> getBrands();

    Brand getBrand(Long id);

    Brand deleteBrand(Long id);

    Brand editBrand(Long id, Brand brand);

}
