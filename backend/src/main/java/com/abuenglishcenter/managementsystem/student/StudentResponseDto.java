package com.abuenglishcenter.managementsystem.student;

import java.time.LocalDate;

public class StudentResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String parentPhone;
    private LocalDate dateOfBirth;
    private Status status;

    public StudentResponseDto(Long id,String username, String fullName, String parentPhone, LocalDate dateOfBirth, Status status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.parentPhone = parentPhone;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Status getStatus() {
        return status;
    } 
    
}
