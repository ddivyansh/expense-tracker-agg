package com.example.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "et_transactions_seq")
    @SequenceGenerator(name = "et_transactions_seq", sequenceName = "et_transactions_seq", allocationSize = 1)
    @Column(name = "transaction_id")
    private Integer transactionId;

    /*
    name of the foreign key column is category_id
    and it references to primary key of Category
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "note", nullable = false, length = 50)
    private String note;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

}
