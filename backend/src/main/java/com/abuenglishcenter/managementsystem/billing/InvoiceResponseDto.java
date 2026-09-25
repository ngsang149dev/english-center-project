package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;

public class InvoiceResponseDto {
    private Long id;
    private Long enrollmentId;
    private Integer month;
    private Integer year;
    private BigDecimal amount;
    private BigDecimal adjustedAmount;
    private Status status;

    public InvoiceResponseDto(Long id, Long enrollmentId, Integer month, Integer year, BigDecimal amount,
            BigDecimal adjustedAmount, Status status) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.adjustedAmount = adjustedAmount;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public Integer getMonth() {
        return month;
    }
    public Integer getYear() {
        return year;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public BigDecimal getAdjustedAmount() {
        return adjustedAmount;
    }
    public Status getStatus() {
        return status;
    }

    
}
