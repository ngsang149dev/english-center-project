package com.abuenglishcenter.managementsystem.attendance;

public class AttendanceItemDto {
    private Long enrollmentId;
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
