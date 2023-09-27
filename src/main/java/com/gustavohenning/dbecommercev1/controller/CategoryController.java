package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.dto.CategoryDTO;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {this.categoryService = categoryService;
    }

    @Operation(summary = "Get All Categories", description = "Get All Categories in DB")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        List<CategoryDTO> categoriesDto = categories.stream().map(CategoryDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);
    }

    @Operation(summary = "Get Category By ID", description = "Get Category from ID in DB")
    @GetMapping(value = "{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable final Long id) {
        Category category = categoryService.getCategory(id);
        return new ResponseEntity<>(CategoryDTO.from(category), HttpStatus.OK);
    }

    @Operation(summary = "Get Categories By Name", description = "Get Categories By Name or Partial Name")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<CategoryDTO>> searchCategoriesByName(@PathVariable String name) {
        List<Category> categories = (List<Category>) categoryService.findByNameContainingIgnoreCase(name);
        List<CategoryDTO> categoryDtos = categories.stream().map(CategoryDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @Operation(summary = "Add New Category", description = "Add a new Category to DB")
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody final CategoryDTO categoryDto) {
        Category category = categoryService.addCategory(Category.from(categoryDto));
        return new ResponseEntity<>(CategoryDTO.from(category), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Category by ID", description = "Update the Category in DB")
    @PutMapping(value = "{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CategoryDTO> editCategory(@PathVariable final Long id, @RequestBody final CategoryDTO categoryDto) {
        Category editedCategory = categoryService.editCategory(id, Category.from(categoryDto));
        return new ResponseEntity<>(CategoryDTO.from(editedCategory), HttpStatus.OK);

    }

    @Operation(summary = "Delete Category by ID", description = "Delete the Category from ID in DB")
    @DeleteMapping(value = "{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable final Long id) {
        Category category = categoryService.deleteCategory(id);
        return new ResponseEntity<>(CategoryDTO.from(category), HttpStatus.OK);
    }
}
