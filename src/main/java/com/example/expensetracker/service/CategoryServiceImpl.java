package com.example.expensetracker.service;

import com.example.expensetracker.domain.Category;
import com.example.expensetracker.entity.CategoryEntity;
import com.example.expensetracker.entity.TransactionEntity;
import com.example.expensetracker.entity.UserEntity;
import com.example.expensetracker.exceptions.EtBadRequestException;
import com.example.expensetracker.exceptions.EtResourceNotFoundException;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        List<CategoryEntity> categoryEntityList = categoryRepository.findByUserEntity_UserId(userId);
        List<Category> categoryList = new ArrayList<>();
        BigDecimal sumOfTransactions;
        for (CategoryEntity categoryEntity : categoryEntityList) {
            //for single category
            sumOfTransactions = BigDecimal.ZERO;
            List<TransactionEntity> transactionEntityList = transactionRepository.findByUserEntity_UserIdAndCategoryEntity_CategoryId(userId, categoryEntity.getCategoryId());
            for (TransactionEntity transactionEntity : transactionEntityList) {
                sumOfTransactions = sumOfTransactions.add(transactionEntity.getAmount());
            }
            Category category = mapper.convertValue(categoryEntity, Category.class);
            category.setTotalExpense(sumOfTransactions.doubleValue());
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findByCategoryIdAndUserEntity_UserId(categoryId, userId);
        if (optionalCategoryEntity.isPresent()) {
            CategoryEntity categoryEntity = optionalCategoryEntity.get();
            BigDecimal sumOfTransactions = BigDecimal.ZERO;
            //fetch the list of transactions made by user and for this particular category
            List<TransactionEntity> transactionEntityList = transactionRepository.findByUserEntity_UserIdAndCategoryEntity_CategoryId(userId, categoryId);
            for (TransactionEntity transactionEntity : transactionEntityList) {
                sumOfTransactions = sumOfTransactions.add(transactionEntity.getAmount());
            }
            Category category = mapper.convertValue(categoryEntity, Category.class);
            category.setTotalExpense(sumOfTransactions.doubleValue());
            return category;
        }
        return null;
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException {
        CategoryEntity categoryEntity = new CategoryEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        categoryEntity.setUserEntity(userEntity);
        categoryEntity.setDescription(description);
        categoryEntity.setTitle(title);
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        Category savedCategory = mapper.convertValue(savedEntity, Category.class);
        return fetchCategoryById(userId, savedCategory.getCategoryId());
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category newCategory) throws EtBadRequestException {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByCategoryIdAndUserEntity_UserId(categoryId, userId);
        if (categoryEntityOptional.isPresent()) {
            CategoryEntity categoryEntity = categoryEntityOptional.get();
            categoryEntity.setTitle(newCategory.getTitle());
            categoryEntity.setDescription(newCategory.getDescription());
            categoryRepository.save(categoryEntity);
        }
    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        transactionRepository.deleteByCategoryEntity_CategoryId(categoryId);
        categoryRepository.deleteByUserEntity_UserIdAndCategoryId(userId, categoryId);
    }
}
