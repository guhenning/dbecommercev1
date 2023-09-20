package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Category;

import java.util.List;

public interface CategoryService {

    Category addCategory(Category category);

    List<Category> getCategories();

    Iterable<Category> findByNameContainingIgnoreCase(String name);

    Category getCategory(Long id);

    Category deleteCategory(Long id);

    Category editCategory(Long id, Category category);
}
