package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class EnrollmentUpdateRequestDto {
    @NotNull 
    private LocalDate enrolledDate;

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

}
