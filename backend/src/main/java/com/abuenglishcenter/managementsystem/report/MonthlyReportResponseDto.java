package com.abuenglishcenter.managementsystem.report;

import java.math.BigDecimal;

public class MonthlyReportResponseDto {
    private Integer year;
    private Integer month;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal profit;
    private BigDecimal teacherSalary;
    private BigDecimal otherExpense;

    public MonthlyReportResponseDto(Integer year, Integer month, BigDecimal totalRevenue, BigDecimal totalExpense,
            BigDecimal profit, BigDecimal teacherSalary, BigDecimal otherExpense) {
        this.year = year;
        this.month = month;
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
        this.profit = profit;
        this.teacherSalary = teacherSalary;
        this.otherExpense = otherExpense;
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


    public BigDecimal getTeacherSalary() {
        return teacherSalary;
    }


    public BigDecimal getOtherExpense() {
        return otherExpense;
    }

    
    
}
