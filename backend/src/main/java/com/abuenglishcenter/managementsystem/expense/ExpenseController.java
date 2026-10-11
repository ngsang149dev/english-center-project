package com.abuenglishcenter.managementsystem.expense;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping 
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @PostMapping 
    public ExpenseResponseDto createExpense(@Valid @RequestBody ExpenseCreateRequestDto request) {
        return expenseService.createExpense(request);
    } 
}
