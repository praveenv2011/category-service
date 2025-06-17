package com.sample.category.controller;

import com.sample.category.dto.CategoryDTO;
import com.sample.category.exception.ApplicationException;
import com.sample.category.exception.CategoryDataAccessException;
import com.sample.category.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories/")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody @Valid CategoryDTO category) throws CategoryDataAccessException, ApplicationException {
        logger.info("Request received to save the category with name {}", category.getCategoryName());
        CategoryDTO savedCategory = categoryService.save(category);
        logger.info("Category saved successfully with name: {}", savedCategory.getCategoryName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getCategoryId())
                .toUri();
        return ResponseEntity.created(location).body(savedCategory);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable @Valid @Positive Long id,
                                                     @RequestBody CategoryDTO category) throws CategoryDataAccessException, ApplicationException {

        logger.info("Request received to update the category with id {}", id);
        CategoryDTO updatedCategory = categoryService.updateById(id,category);

        return ResponseEntity.ok().body(updatedCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() throws CategoryDataAccessException, ApplicationException {
        logger.info("Request received to fetch all categories");
        List<CategoryDTO> categories = categoryService.getAll();
        logger.info("Retrieved {} categories successfully", categories.size());
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable @Positive Long id) throws CategoryDataAccessException, ApplicationException {
        logger.info("Request received to fetch category with id {}", id);
        CategoryDTO category = categoryService.getById(id);
        logger.info("Category with id {} fetched successfully", id);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("search")
    public ResponseEntity<CategoryDTO> getCategoryByName(@RequestParam String name) throws CategoryDataAccessException, ApplicationException {
        logger.info("Request received to fetch category with name {}", name);
        CategoryDTO category = categoryService.getByName(name);
        logger.info("Category with name {} fetched successfully", name);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) throws CategoryDataAccessException, ApplicationException {
        logger.info("Request received to delete category with id {}", id);
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("category deleted successfully");
    }

}
