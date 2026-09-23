package com.abuenglishcenter.managementsystem.attendance;

public class AttendanceItemDto {
    private Long enrollmentId;
    private Status status;
    
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
 
}
