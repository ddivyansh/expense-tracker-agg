package com.example.expensetracker.repository;

import com.example.expensetracker.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    /*
    JPA will generate the required query for us, we just have to use proper name !!!
     */
    Optional<UserEntity> findByEmail(String email);
}
