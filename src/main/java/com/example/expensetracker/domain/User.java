package com.example.expensetracker.domain;

import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class User {
    Integer userId;
    String firstName;
    String lastName;
    String email;
    String password;
}
