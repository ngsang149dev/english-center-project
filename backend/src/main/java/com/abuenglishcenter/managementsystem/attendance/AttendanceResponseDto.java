package com.abuenglishcenter.managementsystem.attendance;

public class AttendanceResponseDto {
    private Long id;
    private Long enrollmentId;
    private Long sessionId;
    private AttendanceStatus status;
    
    public AttendanceResponseDto(Long id, Long enrollmentId, Long sessionId, AttendanceStatus status) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.sessionId = sessionId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

}
