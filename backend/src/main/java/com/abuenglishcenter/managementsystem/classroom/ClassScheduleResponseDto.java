package com.abuenglishcenter.managementsystem.classroom;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ClassScheduleResponseDto {
    private Long id;
    private Long classId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;

    public ClassScheduleResponseDto(Long id, Long classId, DayOfWeek dayOfWeek, LocalTime startTime) {
        this.id = id;
        this.classId = classId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public Long getClassId() {
        return classId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }    
}
