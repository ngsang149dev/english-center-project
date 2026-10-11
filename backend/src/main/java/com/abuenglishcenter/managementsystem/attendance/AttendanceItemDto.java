package com.abuenglishcenter.managementsystem.attendance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AttendanceItemDto {
    @NotNull
    @Positive
    private Long enrollmentId;
    @NotNull
    private AttendanceStatus status;
    
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    public AttendanceStatus getStatus() {
        return status;
    }
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
 
}
