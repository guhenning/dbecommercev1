package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.CategoryNotFoundException;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); }

    @Test
    void testAddCategory() {
        Category categoryToAdd = new Category(1L, "Categor1", null, null, null);

        when(categoryRepository.save(categoryToAdd)).thenReturn(categoryToAdd);

        Category addedCategory = categoryService.addCategory(categoryToAdd);

        assertEquals(categoryToAdd, addedCategory);
    }

    @Test
    void testGetCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Category1", null, null, null));
        categories.add(new Category(2L, "Category2", null, null, null));

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> retrievedCategories = categoryService.getCategories();

        assertEquals(categories.size(), retrievedCategories.size());
    }

    @Test
    void testGetCategory() {
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Category1", null, null, null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category retrievedCategory = categoryService.getCategory(categoryId);

        assertEquals(category, retrievedCategory);
    }

    @Test
    void testGetCategory_WithInvalidId_ShouldThrowException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(categoryId));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
      Category category1 = new Category(1L, "Category1", null, null, null);
      Category category2 = new Category(2L, "Category2", null, null, null);
      Category category3 = new Category(3L, "Another", null, null, null);

      when(categoryRepository.findByNameContainingIgnoreCase("category")).thenReturn(Arrays.asList(category1, category2));

      Iterable<Category> foundCategories = categoryService.findByNameContainingIgnoreCase("category");

      List<Category> expectedCategories = Arrays.asList(category1, category2);
      assertEquals(expectedCategories, foundCategories);
    }


    @Test
    void testDeleteCategory() {
        Long categoryId = 1L;
        Category categoryToDelete = new Category(1L, "Category1", null, null, null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToDelete));
        when(itemService.getItemsByCategoryIds(List.of(categoryId))).thenReturn(new ArrayList<>());

        Category deletedCategory = categoryService.deleteCategory(categoryId);

        assertEquals(categoryToDelete, deletedCategory);
        verify(categoryRepository, times(1)).delete(categoryToDelete);
    }

    @Test
    void testDeleteCategories_WithAssociatedItems_ShouldThrowException() {
        Long categoryId = 1L;
        Category categoryToDelete = new Category(1L, "Category1", null, null, null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToDelete));
        List<Item> itemsWithCategory = new ArrayList<>();
        itemsWithCategory.add(new Item());
        when(itemService.getItemsByCategoryIds(List.of(categoryId))).thenReturn(itemsWithCategory);

        assertThrows(DataIntegrityViolationException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    void TestEditCategory() {
        Long categoryId = 1L;
        Category existingCategory = new Category(categoryId, "Category1", null, null, null);
        Category updatedCategory = new Category(categoryId, "UpdatedCategory", null, null, null);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        Category editedCategory = categoryService.editCategory(categoryId, updatedCategory);

        assertEquals(updatedCategory.getName(), editedCategory.getName());
        assertNotNull(editedCategory.getUpdatedDate());
    }
}