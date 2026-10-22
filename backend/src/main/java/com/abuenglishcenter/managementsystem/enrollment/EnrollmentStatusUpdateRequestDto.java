package com.abuenglishcenter.managementsystem.enrollment;

import jakarta.validation.constraints.NotNull;

public class EnrollmentStatusUpdateRequestDto {
    @NotNull 
    private EnrollmentStatus status;

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

}
