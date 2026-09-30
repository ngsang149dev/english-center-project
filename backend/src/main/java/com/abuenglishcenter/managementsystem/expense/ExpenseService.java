package com.abuenglishcenter.managementsystem.expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class ExpenseService {
    @Autowired 
    private ExpenseRepository expenseRepository;

    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::toDto).toList();
    }

    public ExpenseResponseDto createExpense(ExpenseCreateRequestDto request) {
        Expense newExpense = new Expense();
        newExpense.setAmount(request.getAmount());
        newExpense.setCategory(request.getCategory());
        newExpense.setDescription(request.getDescription());
        newExpense.setExpenseDate(request.getExpenseDate());

        Expense saved = expenseRepository.save(newExpense);
        return toDto(saved);
    }

    private ExpenseResponseDto toDto(Expense expense) {
        return new ExpenseResponseDto(expense.getId(), expense.getCategory(), expense.getDescription(), expense.getAmount(), expense.getExpenseDate());
    }
}
