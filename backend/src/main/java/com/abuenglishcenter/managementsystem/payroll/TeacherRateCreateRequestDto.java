package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TeacherRateCreateRequestDto {
    @NotNull
    @Positive
    private Long teacherId;
    @NotNull
    @Positive
    private Long classroomId;
    @NotNull
    @Positive
    private BigDecimal ratePerSession;
    @NotNull
    private LocalDate effectiveFrom;

    public Long getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    public Long getClassroomId() {
        return classroomId;
    }
    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }
    public BigDecimal getRatePerSession() {
        return ratePerSession;
    }
    public void setRatePerSession(BigDecimal ratePerSession) {
        this.ratePerSession = ratePerSession;
    }
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
