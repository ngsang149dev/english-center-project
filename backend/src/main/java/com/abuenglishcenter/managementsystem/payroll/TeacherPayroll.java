package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;

import com.abuenglishcenter.managementsystem.teacher.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(name = "teacher_payrolls", uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "month", "year"}))
public class TeacherPayroll {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne 
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private BigDecimal sessionPay;
    private BigDecimal bonus;
    private BigDecimal totalPay;
    private boolean isManuallyAdjusted = false;
    private Integer month;
    private Integer year;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status = PayrollStatus.DRAFT;

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

    public BigDecimal getSessionPay() {
        return sessionPay;
    }

    public void setSessionPay(BigDecimal sessionPay) {
        this.sessionPay = sessionPay;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(BigDecimal totalPay) {
        this.totalPay = totalPay;
    }

    public boolean isManuallyAdjusted() {
        return isManuallyAdjusted;
    }

    public void setManuallyAdjusted(boolean isManuallyAdjusted) {
        this.isManuallyAdjusted = isManuallyAdjusted;
    }

    public PayrollStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollStatus status) {
        this.status = status;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    
}
