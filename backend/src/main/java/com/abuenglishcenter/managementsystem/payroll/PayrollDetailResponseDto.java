package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;

public class PayrollDetailResponseDto {
    private Long id;
    private Long payrollId;
    private Long classroomId;
    private Integer sessionsTaught;
    private BigDecimal rateApplied;
    private BigDecimal subtotal;

    public PayrollDetailResponseDto(Long id, Long payrollId, Long classroomId, Integer sessionsTaught,
            BigDecimal rateApplied, BigDecimal subtotal) {
        this.id = id;
        this.payrollId = payrollId;
        this.classroomId = classroomId;
        this.sessionsTaught = sessionsTaught;
        this.rateApplied = rateApplied;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public Long getPayrollId() {
        return payrollId;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public Integer getSessionsTaught() {
        return sessionsTaught;
    }

    public BigDecimal getRateApplied() {
        return rateApplied;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
}
