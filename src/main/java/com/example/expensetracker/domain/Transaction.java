package com.example.expensetracker.domain;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Transaction {
    Integer transactionId;
    Integer categoryId;
    Integer userId;
    Double amount;
    String note;
    Long transactionDate;
}
