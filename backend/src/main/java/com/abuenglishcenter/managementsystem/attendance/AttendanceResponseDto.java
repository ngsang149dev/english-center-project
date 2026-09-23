package com.abuenglishcenter.managementsystem.attendance;

public class AttendanceResponseDto {
    private Long id;
    private Long enrollmentId;
    private Long sessionId;
    private Status status;
    
    public AttendanceResponseDto(Long id, Long enrollmentId, Long sessionId, Status status) {
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

    public Status getStatus() {
        return status;
    }

}
