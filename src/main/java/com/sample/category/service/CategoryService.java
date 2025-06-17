package com.sample.category.service;

import com.sample.category.dto.CategoryDTO;
import com.sample.category.exception.ApplicationException;
import com.sample.category.exception.CategoryNotFoundException;
import com.sample.category.exception.CategoryDataAccessException;
import com.sample.category.model.Category;
import com.sample.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) throws CategoryDataAccessException, ApplicationException {

        logger.info("Saving category with name {}", categoryDTO.getCategoryName());

        Category category = modelMapper.map(categoryDTO, Category.class);

        try {

            categoryRepository.save(category);
            return modelMapper.map(category,CategoryDTO.class);

        }catch (DataAccessResourceFailureException e){
            logger.error("Database connection failure while saving category with name {}", categoryDTO.getCategoryName());
            throw new CategoryDataAccessException("Database connection failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("An unexpected error occurred while saving category with name {}: {}", categoryDTO.getCategoryName(), e.getMessage());
            throw new ApplicationException("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<CategoryDTO> getAll() throws CategoryDataAccessException, ApplicationException {

        logger.info("Fetching all categories");
        List<Category> categories;

        try {
            categories = categoryRepository.findAll();
        }
        catch (DataAccessResourceFailureException e){
            logger.error("Database connection failure while fetching categories");
            throw new CategoryDataAccessException("Database connection failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            logger.error("An unexpected error occurred while fetching categories: {}", e.getMessage());
            throw new ApplicationException("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(categories.isEmpty()){
            logger.warn("No categories found");
            throw new CategoryNotFoundException("categories not found", HttpStatus.NOT_FOUND);
        }

        List<CategoryDTO> categoryDTOs = categories.stream().
                map(product->modelMapper.map(product,CategoryDTO.class)).
                toList();

        logger.info("Found {} categories", categoryDTOs.size());
        return categoryDTOs;

    }

    public CategoryDTO getById(Long id) throws CategoryDataAccessException, ApplicationException {

        logger.info("Fetching category with id {}", id);
        Optional<Category> category;

        try {
             category = categoryRepository.findById(id);
        }catch (DataAccessResourceFailureException e){
            logger.error("Database connection failure while fetching category with id {}", id);
            throw new CategoryDataAccessException("Database connection failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("An unexpected error occurred while fetching category with id {}: {}", id, e.getMessage());
            throw new ApplicationException("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(category.isEmpty()){
            logger.warn("Category with id {} not found", id);
            throw new CategoryNotFoundException("category with id " + id + " not found",HttpStatus.NOT_FOUND);
        }

        Category categoryFromDB = category.get();
        logger.info("Category with id {} found", id);

        return modelMapper.map(categoryFromDB,CategoryDTO.class);
    }

    public CategoryDTO getByName(String name) throws CategoryDataAccessException, ApplicationException {
        logger.info("Fetching category with name {}", name);
        Category category;

        try {
            category = categoryRepository.findByCategoryName(name);
        }catch (DataAccessResourceFailureException e){
            logger.error("Database connection failure while fetching category with name {}", name);

            throw new CategoryDataAccessException("Database connection failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("An unexpected error occurred while fetching category with name {}: {}", name, e.getMessage());
            throw new ApplicationException("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(category == null){
            logger.warn("Category with name {} not found", name);
            throw new CategoryNotFoundException("category with name " + name + " not found",HttpStatus.NOT_FOUND);
        }

        logger.info("Category with name {} found", name);

        return modelMapper.map(category,CategoryDTO.class);

    }

    @Transactional
    public CategoryDTO updateById(Long id,CategoryDTO categoryDTO) throws CategoryDataAccessException, ApplicationException {

        logger.info("Updating category with id {}", id);

        CategoryDTO categoryFromDb = getById(id);
        categoryFromDb.setCategoryName(categoryDTO.getCategoryName());
        CategoryDTO updatedCategory = save(categoryFromDb);

        logger.info("Category with id {} updated successfully", id);
        return updatedCategory;
    }

    @Transactional
    public void deleteById(Long id) throws CategoryDataAccessException, ApplicationException {
        CategoryDTO categoryDTO = getById(id);
        logger.info("Deleting category with id {}", id);

        try {
            categoryRepository.deleteById(id);
        } catch (DataAccessResourceFailureException e) {
            logger.error("Database connection failure while deleting category with id {}", id);
            throw new CategoryDataAccessException("Database connection failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("An unexpected error occurred while deleting category with id {}: {}", id, e.getMessage());
            throw new ApplicationException("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Category with id {} deleted successfully", id);
    }



}
