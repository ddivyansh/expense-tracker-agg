package com.example.expensetracker.repository;

import com.example.expensetracker.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Integer> {
    // it's a list since the user can make multiple transactions for given category
    List<TransactionEntity> findByUserEntity_UserIdAndCategoryEntity_CategoryId(Integer userId, Integer CategoryId);

    void deleteByCategoryEntity_CategoryId(Integer categoryId);
}
