package com.abuenglishcenter.managementsystem.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TeacherUpdateRequestDto {
    @NotBlank
    @Size(max = 255)
    private String fullName;
    @Size(max = 255)
    private String specialization;
    @Pattern(regexp = "\\+?[0-9 ]{8,15}", message = "Phone must have 8-15 digits/spaces and may start with +")
    private String phone;

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}