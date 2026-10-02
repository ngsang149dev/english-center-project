package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;

public class ClassSessionResponseDto {
    private Long id;
    private Long classId;
    private Long teacherId;
    private LocalDate sessionDate;
    private boolean teacherTaught;
    
    public ClassSessionResponseDto(Long id, Long classId, Long teacherId, LocalDate sessionDate, boolean teacherTaught) {
        this.id = id;
        this.classId = classId;
        this.teacherId = teacherId;
        this.sessionDate = sessionDate;
        this.teacherTaught = teacherTaught;
    }

    public Long getId() {
        return id;
    }
    public Long getClassId() {
        return classId;
    }
    public LocalDate getSessionDate() {
        return sessionDate;
    }
    public boolean isTeacherTaught() {
        return teacherTaught;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    
}
