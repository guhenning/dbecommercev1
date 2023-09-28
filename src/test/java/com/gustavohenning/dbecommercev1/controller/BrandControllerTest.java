package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.dto.BrandDTO;
import com.gustavohenning.dbecommercev1.service.BrandService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BrandControllerTest {

    @InjectMocks
    private BrandController brandController;

    @Mock
    private BrandService brandService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBrands() {
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1L, "Brand1", null, null));
        brands.add(new Brand(2L, "Brand2", null, null));

        when(brandService.getBrands()).thenReturn(brands);

        ResponseEntity<List<BrandDTO>> response = brandController.getBrands();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetBrandById() {
        Brand brand = new Brand(1L, "Brand1", null, null);

        when(brandService.getBrand(1L)).thenReturn(brand);

        ResponseEntity<BrandDTO> response = brandController.getBrand(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(brand.getName(), response.getBody().getName());
    }

    @Test
   public void testSearchBrandsByName() {
        List<Brand> brands = new ArrayList<>();
        Brand brand1 = new Brand(1L, "Brand1", null, null);
        Brand brand2 = new Brand(2L, "Brand2", null, null);
        Brand brand3 = new Brand(3L, "Another", null, null);

        when(brandService.findByNameContainingIgnoreCase("brand")).thenReturn(Arrays.asList(brand1, brand2));

        ResponseEntity<List<BrandDTO>> response = brandController.searchBrandsByName("brand");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testAddBrand() {
        BrandDTO brandDTO = new BrandDTO(null, "NewBrand", null, null);
        Brand brand = Brand.from(brandDTO);

        when(brandService.addBrand(brand)).thenReturn(new Brand(1L, "NewBrand", null, null));

        ResponseEntity<BrandDTO> response = brandController.addBrand(brandDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new BrandDTO(1L, "NewBrand", null, null), response.getBody());
    }

    @Test
    public void testEditBrand() {
        Long brandId = 1L;
        BrandDTO updatedBrandDto = new BrandDTO(null, "UpdatedBrand", null, null);
        Brand updatedBrand = Brand.from(updatedBrandDto);

        when(brandService.editBrand(brandId, updatedBrand)).thenReturn(updatedBrand);

        ResponseEntity<BrandDTO> response = brandController.editBrand(brandId, updatedBrandDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBrandDto, response.getBody());
        assertEquals(updatedBrand.getName(), response.getBody().getName());
        System.out.println(response.getBody());

    }

    @Test
   public void testDeleteBrand() {
        Long brandId = 1L;
        BrandDTO deletedBrandDTO = new BrandDTO(null, "DeletedBrand", null, null);
        Brand deletedBrand = Brand.from(deletedBrandDTO);

        when(brandService.deleteBrand(brandId)).thenReturn(deletedBrand);

        ResponseEntity<BrandDTO> response = brandController.deleteBrand(brandId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedBrandDTO, response.getBody());
    }
}