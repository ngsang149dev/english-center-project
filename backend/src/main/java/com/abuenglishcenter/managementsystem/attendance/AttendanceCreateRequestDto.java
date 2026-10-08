package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AttendanceCreateRequestDto {
    @NotNull
    @Positive
    private Long sessionId;

    /*{
  "sessionId": 1,
  "attendances": [
    {"enrollmentId": 1, "status": "PRESENT"},
    {"enrollmentId": 2, "status": "ABSENT"},
    {"enrollmentId": 3, "status": "LATE"}
        ]
    }
   */
    @NotEmpty
    @Valid
    private List<AttendanceItemDto> attendances;

    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    public List<AttendanceItemDto> getAttendances() {
        return attendances;
    }
    public void setAttendances(List<AttendanceItemDto> attendances) {
        this.attendances = attendances;
    }
   
}
