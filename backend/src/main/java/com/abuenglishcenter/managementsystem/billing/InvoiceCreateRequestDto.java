package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class InvoiceCreateRequestDto {
    @NotNull
    @Positive
    private Long enrollmentId;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;
    @NotNull
    @Positive
    @Size(min = 2000, max = 2100)
    private Integer year;
    @Positive
    private BigDecimal amount;

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    
}
