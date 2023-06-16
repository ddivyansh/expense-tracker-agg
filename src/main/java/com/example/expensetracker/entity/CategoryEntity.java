package com.example.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "et_categories_seq")
    @SequenceGenerator(name = "et_categories_seq", sequenceName = "et_categories_seq", allocationSize = 1)
    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_Id")
    private UserEntity userEntity;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "description", nullable = false, length = 50)
    private String description;
}
