package com.example.expensetracker.domain;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Category {
    Integer categoryId;
    Integer userId;
    String title;
    String description;
    Double totalExpense;
}
