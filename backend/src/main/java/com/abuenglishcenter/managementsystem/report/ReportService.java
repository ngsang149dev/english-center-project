package com.abuenglishcenter.managementsystem.report;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.billing.PaymentRepository;
import com.abuenglishcenter.managementsystem.expense.ExpenseRepository;

@Service 
public class ReportService {
    @Autowired 
    PaymentRepository paymentRepository;

    @Autowired 
    ExpenseRepository expenseRepository;

    public MonthlyReportResponseDto getMonthlyReport(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        BigDecimal totalRevenue = paymentRepository.sumValidPaymentsBetween(startDate, endDate);
        BigDecimal totalExpense = expenseRepository.sumExpensesBetween(startDate, endDate);
        
        BigDecimal profit = totalRevenue.subtract(totalExpense);

        return new MonthlyReportResponseDto(year, month, totalRevenue, totalExpense, profit);
    }
}
