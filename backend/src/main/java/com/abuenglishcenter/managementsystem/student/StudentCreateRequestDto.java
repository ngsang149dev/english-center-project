package com.abuenglishcenter.managementsystem.student;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentCreateRequestDto {
    @NotBlank
    @Size(max = 255)
    private String username;
    @NotBlank
    @Size(max = 255)
    private String password;
    @NotBlank
    @Size(max = 255)
    private String fullName;
    @Size(max = 30)
    private String parentPhone;
    @Past
    private LocalDate dateOfBirth;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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
}
