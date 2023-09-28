package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.BrandNotFoundException;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
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

public class BrandServiceImplTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testAddBrand() {
        Brand brandToAdd = new Brand(1L, "Brand1", null, null);

        when(brandRepository.save(brandToAdd)).thenReturn(brandToAdd);

        Brand addedBrand = brandService.addBrand(brandToAdd);

        assertEquals(brandToAdd, addedBrand);
    }

    @Test
     void testGetBrands() {
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1L, "Brand1", null, null));
        brands.add(new Brand(2L, "Brand2", null, null));

        when(brandRepository.findAll()).thenReturn(brands);

        List<Brand> retrievedBrands = brandService.getBrands();

        assertEquals(brands.size(), retrievedBrands.size());
    }

    @Test
     void testGetBrand() {
        Long brandId = 1L;
        Brand brand = new Brand(brandId, "Brand1", null, null);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        Brand retrievedBrand = brandService.getBrand(brandId);

        assertEquals(brand, retrievedBrand);
    }

    @Test
    void testGetBrand_WithInvalidId_ShouldThrowException() {
        Long brandId = 1L;

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.getBrand(brandId));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {

        Brand brand1 = new Brand(1L, "Brand1", null, null);
        Brand brand2 = new Brand(2L, "Brand2", null, null);
        Brand brand3 = new Brand(3L, "Another", null, null);

        when(brandRepository.findByNameContainingIgnoreCase("brand")).thenReturn(Arrays.asList(brand1, brand2));

        Iterable<Brand> foundBrands = brandService.findByNameContainingIgnoreCase("brand");

        List<Brand> expectedBrands = Arrays.asList(brand1, brand2);
        assertEquals(expectedBrands, foundBrands);
    }

    @Test
    void testDeleteBrand() {
        Long brandId = 1L;
        Brand brandToDelete = new Brand(brandId, "Brand1", null, null);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandToDelete));
        when(itemService.getItemsByBrandId(brandId)).thenReturn(new ArrayList<>());

        Brand deletedBrand = brandService.deleteBrand(brandId);

        assertEquals(brandToDelete, deletedBrand);
        verify(brandRepository, times(1)).delete(brandToDelete);
    }

    @Test
    void testDeleteBrand_WithAssociatedItems_ShouldThrowException() {
        Long brandId = 1L;
        Brand brandToDelete = new Brand(brandId, "Brand1", null, null);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandToDelete));
        List<Item> itemsWithBrand = new ArrayList<>();
        itemsWithBrand.add(new Item());
        when(itemService.getItemsByBrandId(brandId)).thenReturn(itemsWithBrand);

        assertThrows(DataIntegrityViolationException.class, () -> brandService.deleteBrand(brandId));
    }

    @Test
    void testEditBrand() {
        Long brandId = 1L;
        Brand existingBrand = new Brand(brandId, "Brand1", null, null);
        Brand updatedBrand= new Brand(brandId, "UpdatedBrand", null, null);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));

        Brand editedBrand = brandService.editBrand(brandId, updatedBrand);

        assertEquals(updatedBrand.getName(), editedBrand.getName());
        assertNotNull(editedBrand.getUpdatedDate());
    }
}