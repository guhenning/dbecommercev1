package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.dto.BrandDTO;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
import com.gustavohenning.dbecommercev1.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brand")
@CrossOrigin("*")
public class BrandController {

    private final BrandService brandService;
    private final BrandRepository brandRepository;

    @Autowired
    public BrandController(BrandService brandService, BrandRepository brandRepository) {this.brandService = brandService;
        this.brandRepository = brandRepository;
    }



   // @Operation(summary = "Get All Brands", description = "Get All Brands in DB")
    @GetMapping
    public ResponseEntity<List<BrandDTO>> getBrands() {
        List<Brand> brands = brandService.getBrands();
        List<BrandDTO> brandsDto = brands.stream().map(BrandDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(brandsDto, HttpStatus.OK);
    }

    //@Operation(summary = "Get Brand By ID", description = "Get Brand from ID in DB")
    @GetMapping(value = "{id}")
    public ResponseEntity<BrandDTO> getBrand(@PathVariable final Long id) {
        Brand brand = brandService.getBrand(id);
        return new ResponseEntity<>(BrandDTO.from(brand), HttpStatus.OK);
    }

    //@Operation(summary = "Get Brands By Name", description = "Get Brands By Name or Partial Name")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<BrandDTO>> searchBrandsByName(@PathVariable String name) {
        List<Brand> brands = (List<Brand>) brandRepository.findByNameContainingIgnoreCase(name);
        List<BrandDTO> brandDtos = brands.stream().map(BrandDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(brandDtos, HttpStatus.OK);
    }

    // @Operation(summary = "Add New Brand", description = "Add a new Brand to DB")
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BrandDTO> addBrand(@RequestBody final BrandDTO brandDto) {
        Brand brand = brandService.addBrand(Brand.from(brandDto));
        return new ResponseEntity<>(BrandDTO.from(brand), HttpStatus.CREATED);
    }

    //@Operation(summary = "Update Brand by ID", description = "Update the Brand in DB")
    @PutMapping(value = "{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BrandDTO> editBrand(@PathVariable final Long id, @RequestBody final BrandDTO brandDto) {
        Brand editedBrand = brandService.editBrand(id, Brand.from(brandDto));
        return new ResponseEntity<>(BrandDTO.from(editedBrand), HttpStatus.OK);

    }

    //@Operation(summary = "Delete Brand by ID", description = "Delete the Brand from ID in DB")
    @DeleteMapping(value = "{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BrandDTO> deleteBrand(@PathVariable final Long id) {
        Brand brand = brandService.deleteBrand(id);
        return new ResponseEntity<>(BrandDTO.from(brand), HttpStatus.OK);
    }



}
