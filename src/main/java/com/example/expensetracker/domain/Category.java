package com.example.expensetracker.domain;

import lombok.*;

@Data
public class Category {
    Integer categoryId;
    Integer userId;
    String title;
    String description;
    Double totalExpense;
}
