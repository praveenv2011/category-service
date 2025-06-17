package com.sample.category.service;

import com.sample.category.dto.CategoryDTO;
import com.sample.category.exception.ApplicationException;
import com.sample.category.exception.CategoryDataAccessException;
import com.sample.category.exception.CategoryNotFoundException;
import com.sample.category.model.Category;
import com.sample.category.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

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
    public void categoryService_save_returnSavedCategoryDTO() throws CategoryDataAccessException, ApplicationException {

        //act
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        when(modelMapper.map(any(CategoryDTO.class),eq(Category.class)))
                .thenReturn(category);

        when(modelMapper.map(Mockito.any(Category.class),Mockito.eq(CategoryDTO.class)))
                .thenReturn(categoryDTO);

        CategoryDTO savedCategory = categoryService.save(categoryDTO);

        //assert
        Assertions.assertThat(savedCategory).isNotNull();
    }

    @Test
    public void categoryService_getAll_returnAllCategories() throws CategoryDataAccessException, ApplicationException {

       when(categoryRepository.findAll())
               .thenReturn(List.of(category));

       when(modelMapper.map(Mockito.any(Category.class),Mockito.eq(CategoryDTO.class)))
               .thenReturn(categoryDTO);

       List<CategoryDTO> categories = categoryService.getAll();

       Assertions.assertThat(categories).isNotNull();
       Assertions.assertThat(categories.size()).isEqualTo(1);

    }

    @Test
    public void categoryService_getById_returnCategoryDTO() throws CategoryDataAccessException, ApplicationException {

       when(categoryRepository.findById(category.getCategoryId()))
               .thenReturn(Optional.of(category));

       when(modelMapper.map(Mockito.any(Category.class),Mockito.eq(CategoryDTO.class)))
               .thenReturn(categoryDTO);

       CategoryDTO foundCategory = categoryService.getById(category.getCategoryId());

         Assertions.assertThat(foundCategory).isNotNull();
         Assertions.assertThat(foundCategory.getCategoryName()).isEqualTo("Test Category");

    }

    @Test
    public void categoryService_updateById_returnUpdatedCategory() throws CategoryDataAccessException, ApplicationException {

//            // Arrange
//            CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(1L).categoryName("Test Category").build();
//            Category category = Category.builder().categoryId(1L).categoryName("Test Category").build();

            when(categoryRepository.findById(category.getCategoryId())).
                    thenReturn(Optional.of(category));
            when(categoryRepository.save(Mockito.any(Category.class)))
                        .thenReturn(category);
            when(modelMapper.map(Mockito.any(CategoryDTO.class), Mockito.eq(Category.class)))
                        .thenReturn(category);
            when(modelMapper.map(Mockito.any(Category.class), Mockito.eq(CategoryDTO.class)))
                        .thenReturn(categoryDTO);

            // Act
            categoryDTO.setCategoryName("Updated Category");
            CategoryDTO updatedCategory = categoryService.updateById(categoryDTO.getCategoryId(), categoryDTO);

            // Assert
            Assertions.assertThat(updatedCategory).isNotNull();
            Assertions.assertThat(updatedCategory.getCategoryName()).isEqualTo("Updated Category");
    }

    @Test
    public void categoryService_deleteById_returnVoid() throws CategoryDataAccessException, ApplicationException {

     //Category category = Category.builder().categoryId(1l).categoryName("Test Category").build();

     when(categoryRepository.findById(category.getCategoryId()))
             .thenReturn(Optional.of(category));

     doNothing().when(categoryRepository).deleteById(category.getCategoryId());

     categoryService.deleteById(category.getCategoryId());
     CategoryDTO foundCategory = categoryService.getById(category.getCategoryId());

     Assertions.assertThat(foundCategory).isNull();

    }

    @Test
    public void categoryService_getByName_returnCategoryDTO() throws CategoryDataAccessException, ApplicationException {
//        Category category = Category.builder().categoryId(1L).categoryName("Test Category").build();
//        CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(1L).categoryName("Test Category").build();

        when(categoryRepository.findByCategoryName(category.getCategoryName()))
                .thenReturn(category);
        when(modelMapper.map(Mockito.any(Category.class), Mockito.eq(CategoryDTO.class)))
                .thenReturn(categoryDTO);

        CategoryDTO foundCategory = categoryService.getByName(category.getCategoryName());

        Assertions.assertThat(foundCategory).isNotNull();
        Assertions.assertThat(foundCategory.getCategoryName()).isEqualTo(category.getCategoryName());

    }

    @Test
    public void categoryService_categoryNotFound_shouldReturn404(){

        when(categoryRepository.findById(3l))
                .thenThrow(new CategoryNotFoundException("category with id 3l not found", HttpStatus.NOT_FOUND));


        Assertions.assertThatThrownBy(()->categoryRepository.findById(3l))
                .hasMessageContaining("category with id 3l not found")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void categoryService_databaseConnectionFailure_shouldReturn503(){

        when(categoryRepository.findById(1l))
                .thenThrow(new CategoryDataAccessException("Database connection failure while fetching category", HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThatThrownBy(()->categoryRepository.findById(1l))
                .hasMessageContaining("Database connection failure while fetching category")
                .extracting("status")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
