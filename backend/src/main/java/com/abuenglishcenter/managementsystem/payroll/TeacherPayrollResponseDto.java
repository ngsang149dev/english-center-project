package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.util.List;

public class TeacherPayrollResponseDto {
    private Long id;
    private Long teacherId;
    private Integer month;
    private Integer year;
    private BigDecimal sessionPay;
    private BigDecimal bonus;
    private BigDecimal totalPay;
    private boolean isManuallyAdjusted;
    private PayrollStatus status;
    private List<PayrollDetailResponseDto> details;
    
    public TeacherPayrollResponseDto(Long id, Long teacherId, Integer month, Integer year, BigDecimal sessionPay,
            BigDecimal bonus, BigDecimal totalPay, boolean isManuallyAdjusted, PayrollStatus status, List<PayrollDetailResponseDto> details) {
        this.id = id;
        this.teacherId = teacherId;
        this.month = month;
        this.year = year;
        this.sessionPay = sessionPay;
        this.bonus = bonus;
        this.totalPay = totalPay;
        this.isManuallyAdjusted = isManuallyAdjusted;
        this.status = status;
        this.details = details;
    }


    public Long getId() {
        return id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public BigDecimal getSessionPay() {
        return sessionPay;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public BigDecimal getTotalPay() {
        return totalPay;
    }

    public boolean isManuallyAdjusted() {
        return isManuallyAdjusted;
    }

    public PayrollStatus getStatus() {
        return status;
    }

    public List<PayrollDetailResponseDto> getDetails() {
        return details;
    }

    
}
