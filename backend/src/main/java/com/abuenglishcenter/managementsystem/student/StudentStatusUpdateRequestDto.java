package com.abuenglishcenter.managementsystem.student;

import jakarta.validation.constraints.NotNull;

public class StudentStatusUpdateRequestDto {
    @NotNull
    private StudentStatus status;

    public StudentStatus getStatus() {
        return status;
    }
    public void setStatus(StudentStatus status) {
        this.status = status;
    }
}