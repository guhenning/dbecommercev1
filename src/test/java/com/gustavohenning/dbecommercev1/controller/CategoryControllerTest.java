package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.dto.CategoryDTO;
import com.gustavohenning.dbecommercev1.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Category1", null ,null, null));
        categories.add(new Category(1L, "Category2", null ,null, null));

        when(categoryService.getCategories()).thenReturn(categories);

        ResponseEntity<List<CategoryDTO>> response = categoryController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category(1L, "Category1", null, null, null);

        when(categoryService.getCategory(1L)).thenReturn(category);

        ResponseEntity<CategoryDTO> response = categoryController.getCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category.getName(), response.getBody().getName());
    }

    @Test
    void tesSearchCategoriesByName() {
        Category category1 = new Category(1L, "Category1", null, null, null);
        Category category2 = new Category(2L, "Category1", null, null, null);
        Category category3 = new Category(3L, "Another", null, null, null);

        when(categoryService.findByNameContainingIgnoreCase("category")).thenReturn(Arrays.asList(category1, category2));

        ResponseEntity<List<CategoryDTO>> response = categoryController.searchCategoriesByName("category");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(null, "NewCategory", null, null);
        Category category = Category.from(categoryDTO);

        when(categoryService.addCategory(category)).thenReturn(new Category(1L, "NewCategory", null, null, null));

        ResponseEntity<CategoryDTO> response = categoryController.addCategory(categoryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new CategoryDTO(1L, "NewCategory", null, null), response.getBody());

    }

    @Test
    void testEditCategory() {
        Long categoryId = 1L;
        CategoryDTO updatedCategoryDTO = new CategoryDTO(null, "UpdatedCategory", null, null);
        Category updatedCategory = Category.from(updatedCategoryDTO);

        when(categoryService.editCategory(categoryId, updatedCategory)).thenReturn(updatedCategory);

        ResponseEntity<CategoryDTO> response = categoryController.editCategory(categoryId, updatedCategoryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCategoryDTO, response.getBody());
        assertEquals(updatedCategory.getName(), response.getBody().getName());
    }

    @Test
    void testDeleteCategory() {
        Long categoryId = 1L;
        CategoryDTO deletedCategoryDTO = new CategoryDTO(null, "DeletedCategory", null, null);
        Category deletedCategory = Category.from(deletedCategoryDTO);

        when(categoryService.deleteCategory(categoryId)).thenReturn(deletedCategory);

        ResponseEntity<CategoryDTO> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedCategoryDTO, response.getBody());
    }
}