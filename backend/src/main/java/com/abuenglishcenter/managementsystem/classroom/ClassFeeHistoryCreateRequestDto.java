package com.abuenglishcenter.managementsystem.classroom;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClassFeeHistoryCreateRequestDto {
    private Long classId;
    private BigDecimal monthlyFee;
    private LocalDate effectiveFrom;
    
    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }
    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    
}
