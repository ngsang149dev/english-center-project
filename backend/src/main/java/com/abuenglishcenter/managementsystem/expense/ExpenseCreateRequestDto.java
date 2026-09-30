package com.abuenglishcenter.managementsystem.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseCreateRequestDto {
    private ExpenseCategory category;
    private String description;
    private BigDecimal amount;
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
