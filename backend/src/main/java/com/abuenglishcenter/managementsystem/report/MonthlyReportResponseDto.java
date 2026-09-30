package com.abuenglishcenter.managementsystem.report;

import java.math.BigDecimal;

public class MonthlyReportResponseDto {
    private Integer year;
    private Integer month;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal profit;

    public MonthlyReportResponseDto(Integer year, Integer month, BigDecimal totalRevenue, BigDecimal totalExpense,
            BigDecimal profit) {
        this.year = year;
        this.month = month;
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
        this.profit = profit;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    
    
}
