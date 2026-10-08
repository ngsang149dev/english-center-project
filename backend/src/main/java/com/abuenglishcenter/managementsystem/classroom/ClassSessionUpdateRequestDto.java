package com.abuenglishcenter.managementsystem.classroom;

import jakarta.validation.constraints.NotNull;

public class ClassSessionUpdateRequestDto {
    @NotNull
    private Boolean teacherTaught;

    public Boolean getTeacherTaught() {
        return teacherTaught;
    }

    public void setTeacherTaught(Boolean teacherTaught) {
        this.teacherTaught = teacherTaught;
    }
}
