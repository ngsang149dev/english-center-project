package com.abuenglishcenter.managementsystem.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDto {
    private Long id;
    private ExpenseCategory category;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    
    public ExpenseResponseDto(Long id, ExpenseCategory category, String description, BigDecimal amount,
            LocalDate expenseDate) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    public Long getId() {
        return id;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    
}
