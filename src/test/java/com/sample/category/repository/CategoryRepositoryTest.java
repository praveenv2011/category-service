package com.sample.category.repository;

import com.sample.category.model.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void CategoryRepository_Save_ReturnSavedCategory(){
        //Arrange
        Category category = Category.builder()
                .categoryName("Test Category")
                .build();

        //Act
        Category savedCategory = categoryRepository.save(category);

        //Assert
        Assertions.assertThat(savedCategory).isNotNull();
        Assertions.assertThat(savedCategory.getCategoryId()).isGreaterThan(0);
    }

    @Test
    public void CategoryRepository_GetAll_ReturnAllCategories(){
        Category category = Category.builder().categoryName("Test Category").build();
        Category category1 = Category.builder().categoryName("Test Category 1").build();

        categoryRepository.save(category);
        categoryRepository.save(category1);

        List<Category> categories = categoryRepository.findAll();

        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    public void CategoryRepository_GetById_ReturnCategory() {
        Category category = Category.builder()
                .categoryName("Test Category")
                .build();

        categoryRepository.save(category);

        Category savedCategory = categoryRepository.findById(category.getCategoryId())
                .orElse(null);

        Assertions.assertThat(savedCategory).isNotNull();

    }

    @Test
    public void CategoryRepository_UpdateById_ReturnUpdatedCategory() {
        Category category = Category.builder().categoryName("Test Category").build();

        categoryRepository.save(category);

        Category savedCategory = categoryRepository.findById(category.getCategoryId())
                .orElse(null);

        savedCategory.setCategoryName("Updated Category");

        Category updatedCategory = categoryRepository.save(savedCategory);

        Assertions.assertThat(updatedCategory).isNotNull();
        Assertions.assertThat(updatedCategory.getCategoryName()).isEqualTo("Updated Category");
    }

    @Test
    public void CategoryRepository_DeleteById_ReturnNull() {
       Category category = Category.builder().categoryName("Test").build();
       categoryRepository.save(category);

       Long id = category.getCategoryId();
       categoryRepository.deleteById(id);

       Category deletedCategory = categoryRepository.findById(id).orElse(null);

       Assertions.assertThat(deletedCategory).isNull();

    }


}
