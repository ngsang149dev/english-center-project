package com.abuenglishcenter.managementsystem.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ExpenseCreateRequestDto {
    @NotNull
    private ExpenseCategory category;
    @Size(max = 255)
    private String description;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private LocalDate expenseDate;

    public ExpenseCategory getCategory() {
        return category;
    }
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

}
