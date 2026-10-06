package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;

public class StudentBalanceResponseDto {
    private Long studentId;
    private BigDecimal totalBilled;
    private BigDecimal totalPaid;
    private BigDecimal totalOutstanding;
    
    public StudentBalanceResponseDto(Long studentId, BigDecimal totalBilled, BigDecimal totalPaid) {
        this.studentId = studentId;
        this.totalBilled = totalBilled;
        this.totalPaid = totalPaid;
        this.totalOutstanding = totalBilled.subtract(totalPaid);
    }

    public Long getStudentId() {
        return studentId;
    }

    public BigDecimal getTotalBilled() {
        return totalBilled;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public BigDecimal getTotalOutstanding() {
        return totalOutstanding;
    }

    
}
