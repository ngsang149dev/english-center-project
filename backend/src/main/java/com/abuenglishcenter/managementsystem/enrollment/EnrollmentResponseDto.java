package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;

public class EnrollmentResponseDto {

    private Long id;
    private Long studentId;
    private Long classId;
    private LocalDate enrolledDate;
    private EnrollmentStatus status;

    
    public EnrollmentResponseDto(Long id, Long studentId, Long classId, LocalDate enrolledDate, EnrollmentStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.classId = classId;
        this.enrolledDate = enrolledDate;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    public Long getStudentId() {
        return studentId;
    }
    public Long getClassId() {
        return classId;
    }
    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }
    public EnrollmentStatus getStatus() {
        return status;
    }

    
}
