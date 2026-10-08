package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ClassSessionCreateRequestDto {
    @NotNull
    @Positive
    private Long classId;
    @NotNull
    private LocalDate sessionDate;
    private boolean teacherTaught = true;
    @Positive
    private Long teacherId;
    
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public boolean isTeacherTaught() {
        return teacherTaught;
    }

    public void setTeacherTaught(boolean teacherTaught) {
        this.teacherTaught = teacherTaught;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

}
