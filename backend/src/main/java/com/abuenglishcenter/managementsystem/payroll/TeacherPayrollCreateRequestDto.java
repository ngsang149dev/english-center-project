package com.abuenglishcenter.managementsystem.payroll;

public class TeacherPayrollCreateRequestDto {
    private Long teacherId;
    private Integer month;
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
