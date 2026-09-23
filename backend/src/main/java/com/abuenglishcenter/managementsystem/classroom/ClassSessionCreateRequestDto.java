package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;

public class ClassSessionCreateRequestDto {
    private Long classId;
    private LocalDate sessionDate;
    private boolean teacherTaught = true;
    
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

}
