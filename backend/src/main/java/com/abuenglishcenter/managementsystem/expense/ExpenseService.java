package com.abuenglishcenter.managementsystem.expense;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@Service 
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public PageResponseDto<ExpenseResponseDto> getAllExpenses(int page, int size) {
        return PageResponseDto.of(expenseRepository.findAll(PageResponseDto.pageable(page, size)), this::toDto);
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
