package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TeacherRateResponseDto {
    private Long id;
    private Long teacherId;
    private Long classroomId;
    private BigDecimal ratePerSession;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    public TeacherRateResponseDto(Long id, Long teacherId, Long classroomId, BigDecimal ratePerSession,
            LocalDate effectiveFrom, LocalDate effectiveTo) {
        this.id = id;
        this.teacherId = teacherId;
        this.classroomId = classroomId;
        this.ratePerSession = ratePerSession;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }
    
    public Long getId() {
        return id;
    }
    public Long getTeacherId() {
        return teacherId;
    }
    public Long getClassroomId() {
        return classroomId;
    }
    public BigDecimal getRatePerSession() {
        return ratePerSession;
    }
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    
}
