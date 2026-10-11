package com.abuenglishcenter.managementsystem.classroom;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ClassSessionTeacherUpdateRequestDto {
    @NotNull
    @Positive
    private Long teacherId;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
