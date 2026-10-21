package com.abuenglishcenter.managementsystem.attendance;

import jakarta.validation.constraints.NotNull;

public class AttendanceUpdateRequestDto {
    @NotNull 
    private AttendanceStatus status;

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

}
