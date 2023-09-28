package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.CategoryNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.service.CategoryService;
import com.gustavohenning.dbecommercev1.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ItemService itemService) {
        this.categoryRepository = categoryRepository;
        this.itemService = itemService;
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

        List<Item> itemsWithCategory = itemService.getItemsByCategoryIds(Collections.singletonList(id));

        if (!itemsWithCategory.isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete a Category that are associated with an or more Items," +
                    " please modify or delete the Items with this Category");
        }
        categoryRepository.delete(category);

        return category;
    }

    @Transactional
    public Category editCategory(Long id, Category category) {
        Category categoryToEdit = getCategory(id);

        if (category.getName() != null) {
            categoryToEdit.setName(category.getName());
        }

        categoryToEdit.setUpdatedDate(LocalDateTime.now());

        return categoryToEdit;
    }
}