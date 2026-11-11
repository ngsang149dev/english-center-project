package com.abuenglishcenter.managementsystem.student;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentUpdateRequestDto {
    @NotBlank
    @Size(max = 255)
    private String fullName;
    @Pattern(regexp = "\\+?[0-9 ]{8,15}", message = "Phone must have 8-15 digits/spaces and may start with +")
    private String parentPhone;
    @Past
    private LocalDate dateOfBirth;
    @Size(max = 255)
    private String evaluationSheetUrl;

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getParentPhone() {
        return parentPhone;
    }
    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getEvaluationSheetUrl() {
        return evaluationSheetUrl;
    }
    public void setEvaluationSheetUrl(String evaluationSheetUrl) {
        this.evaluationSheetUrl = evaluationSheetUrl;
    }
}