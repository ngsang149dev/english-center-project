package com.abuenglishcenter.managementsystem.classroom;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ClassScheduleCreateRequestDto {
    private Long classId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;

    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

}
