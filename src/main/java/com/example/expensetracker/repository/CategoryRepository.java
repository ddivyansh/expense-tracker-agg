package com.example.expensetracker.repository;

import com.example.expensetracker.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByUserEntity_UserId(Integer userId);

    Optional<CategoryEntity> findByCategoryIdAndUserEntity_UserId(Integer categoryId, Integer userId);

    void deleteByUserEntity_UserIdAndCategoryId(Integer userId, Integer categoryId);
}
