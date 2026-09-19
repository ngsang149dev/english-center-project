package com.abuenglishcenter.managementsystem.teacher;

public class TeacherResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String specialization;
    private String phone;
    
    public TeacherResponseDto(Long id,String username, String fullName, String specialization, String phone) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
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

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    
}
