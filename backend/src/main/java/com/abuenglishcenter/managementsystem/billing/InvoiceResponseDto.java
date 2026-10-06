package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;

public class InvoiceResponseDto {

    private Long id;
    private Long enrollmentId;
    private Integer month;
    private Integer year;
    private BigDecimal amount;
    private BigDecimal adjustedAmount;
    private InvoiceStatus status;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private String studentName;
    private String className;

    public InvoiceResponseDto(Long id, Long enrollmentId, Integer month, Integer year, BigDecimal amount,
            BigDecimal adjustedAmount, InvoiceStatus status, BigDecimal paidAmount, BigDecimal remainingAmount,
            String studentName, String className) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.adjustedAmount = adjustedAmount;
        this.status = status;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.studentName = studentName;
        this.className = className;
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

    public InvoiceStatus getStatus() {
        return status;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getClassName() {
        return className;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }
}
