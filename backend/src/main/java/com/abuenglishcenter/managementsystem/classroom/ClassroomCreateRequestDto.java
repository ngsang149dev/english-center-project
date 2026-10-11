package com.abuenglishcenter.managementsystem.classroom;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ClassroomCreateRequestDto {
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotNull
    @Positive
    private Long teacherId;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    
}
