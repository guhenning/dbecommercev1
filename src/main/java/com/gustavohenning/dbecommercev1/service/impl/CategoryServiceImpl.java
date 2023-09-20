package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.exception.CategoryNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categoryRepository.findAll());
    }

    public Iterable<Category> findByNameContainingIgnoreCase(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category deleteCategory(Long id) {
        Category category = getCategory(id);
        categoryRepository.delete(category);
        return category;
    }

    @Transactional
    public Category editCategory(Long id, Category category) {
        Category categoryToEdit = getCategory(id);

        if (category.getName() != null) {
            categoryToEdit.setName(category.getName());
        }

        LocalDateTime currentUpdateDate = categoryToEdit.getUpdatedDate();
        if (currentUpdateDate == null) {
            categoryToEdit.setUpdatedDate(categoryToEdit.getCreatedDate());
        } else {
            categoryToEdit.setUpdatedDate(category.getUpdatedDate());
        }


        return categoryToEdit;
    }
}