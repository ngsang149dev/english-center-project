package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.abuenglishcenter.managementsystem.classroom.Classroom;
import com.abuenglishcenter.managementsystem.teacher.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "teacher_rates")
public class TeacherRate {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne 
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private BigDecimal ratePerSession;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public Classroom getClassroom() {
        return classroom;
    }
    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
    public BigDecimal getRatePerSession() {
        return ratePerSession;
    }
    public void setRatePerSession(BigDecimal ratePerSession) {
        this.ratePerSession = ratePerSession;
    }
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }
    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

}
