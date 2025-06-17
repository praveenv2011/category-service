package com.sample.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.category.dto.CategoryDTO;
import com.sample.category.exception.CategoryDataAccessException;
import com.sample.category.model.Category;
import com.sample.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModelMapper modelMapper;

    private Category category;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void init(){
        //Arrange
        category = Category.builder()
                .categoryId(1L)
                .categoryName("Test Category")
                .build();
        categoryDTO = CategoryDTO.builder()
                .categoryId(1L)
                .categoryName("Test Category")
                .build();

    }

    @Test
    public void categoryController_saveCategory_returnSavedCategory() throws CategoryDataAccessException, Exception {

        //Act
        when(categoryService.save(any(CategoryDTO.class))).thenReturn(categoryDTO);

        //Assertions
        mockMvc.perform(post("/api/categories/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Test Category"));
    }

    @Test
    public void categoryController_updateCategoryById_returnUpdatedCategory() throws CategoryDataAccessException, Exception {
       CategoryDTO updatedCategory = CategoryDTO.builder()
                .categoryId(1L)
                .categoryName("updated category")
                .build();

            when(categoryService.updateById(eq(categoryDTO.getCategoryId()), any(CategoryDTO.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/"+categoryDTO.getCategoryId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("updated category"));

    }

    @Test
    public void categoryController_getAllCategories_returnAllCategories() throws CategoryDataAccessException, Exception {
        CategoryDTO categoryDTO1 = CategoryDTO.builder().categoryId(2L).categoryName("Category").build();


        when(categoryService.getAll()).thenReturn(Arrays.asList(categoryDTO,categoryDTO1));

        mockMvc.perform(get("/api/categories/"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("Category"))
                .andExpect(status().isOk());

    }

    @Test
    public void categoryController_getCategoryById_returnCategory() throws CategoryDataAccessException, Exception {

        CategoryDTO categoryDTO1 = CategoryDTO.builder().categoryId(2L).categoryName("Category").build();

        when(categoryService.getById(eq(categoryDTO.getCategoryId()))).thenReturn(categoryDTO);

        when(categoryService.getById(eq(categoryDTO1.getCategoryId()))).thenReturn(categoryDTO1);

        mockMvc.perform(get("/api/categories/"+categoryDTO1.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value("Category"))
                .andExpect(status().isOk());

    }

    @Test
    public void categoryController_getCategoryByName_returnCategory() throws CategoryDataAccessException, Exception {

        CategoryDTO categoryDTO1 = CategoryDTO.builder().categoryId(2L).categoryName("Category").build();

        when(categoryService.getByName(eq(categoryDTO.getCategoryName()))).thenReturn(categoryDTO);

        when(categoryService.getByName(eq(categoryDTO1.getCategoryName()))).thenReturn(categoryDTO1);

        mockMvc.perform(get("/api/categories/search?name="+categoryDTO.getCategoryName()))
                .andExpect(jsonPath("$.categoryName").value("Test Category"))
                .andExpect(status().isOk());

    }


    @Test
    public void categoryController_deleteCategoryById_returnCategory() throws CategoryDataAccessException, Exception {
        doNothing().when(categoryService).deleteById(categoryDTO.getCategoryId());

        mockMvc.perform(delete("/api/categories/"+categoryDTO.getCategoryId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.categoryName").doesNotExist());
        verify(categoryService).deleteById(categoryDTO.getCategoryId());
    }



}
