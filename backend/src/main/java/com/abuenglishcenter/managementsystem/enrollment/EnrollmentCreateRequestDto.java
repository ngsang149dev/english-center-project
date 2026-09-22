package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;

public class EnrollmentCreateRequestDto {
    private Long studentId;
    private Long classId;
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
