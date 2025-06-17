package com.sample.category.repository;

import com.sample.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByCategoryName(String name);

    //List<Category> findByIdAndCategoryName(Long id, String name);

}
