package com.abuenglishcenter.managementsystem.payroll;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TeacherPayrollCreateRequestDto {
    @NotNull
    @Positive
    private Long teacherId;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;
    @NotNull
    @Positive
    private Integer year;

    public TeacherPayrollCreateRequestDto() {
    }

    public TeacherPayrollCreateRequestDto(Long teacherId, Integer month, Integer year) {
        this.teacherId = teacherId;
        this.month = month;
        this.year = year;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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

}
