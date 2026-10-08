package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EnrollmentCreateRequestDto {
    @NotNull
    @Positive
    private Long studentId;
    @NotNull
    @Positive
    private Long classId;
    @NotNull
    private LocalDate enrolledDate;
    
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }
    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    
}
