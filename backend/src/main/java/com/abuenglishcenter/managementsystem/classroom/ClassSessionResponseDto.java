package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;

public class ClassSessionResponseDto {
    private Long id;
    private Long classId;
    private LocalDate sessionDate;
    private boolean teacherTaught;
    
    public ClassSessionResponseDto(Long id, Long classId, LocalDate sessionDate, boolean teacherTaught) {
        this.id = id;
        this.classId = classId;
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

    
}
