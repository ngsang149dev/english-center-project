package com.abuenglishcenter.managementsystem.classroom;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClassFeeHistoryResponseDto {
    private Long id;
    private Long classId;
    private BigDecimal monthlyFee;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean isActive;

    public ClassFeeHistoryResponseDto(Long id, Long classId, BigDecimal monthlyFee, LocalDate effectiveFrom,
            LocalDate effectiveTo) {
        this.id = id;
        this.classId = classId;
        this.monthlyFee = monthlyFee;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = (effectiveTo == null);
    }
    

    public Long getId() {
        return id;
    }
    public Long getClassId() {
        return classId;
    }
    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    //effectiveTo == null
    public boolean isActive() {
        return isActive;
    }
}
