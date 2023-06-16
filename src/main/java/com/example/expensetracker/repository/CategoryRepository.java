package com.example.expensetracker.repository;

import com.example.expensetracker.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

}
