package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;

import com.abuenglishcenter.managementsystem.teacher.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "class_sessions")
public class ClassSession {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "class_id")
    private Classroom classroom;

    private LocalDate sessionDate;

    private boolean teacherTaught;

    @ManyToOne 
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


}
