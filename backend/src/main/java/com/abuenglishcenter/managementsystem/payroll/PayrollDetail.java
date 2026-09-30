package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;

import com.abuenglishcenter.managementsystem.classroom.Classroom;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "payroll_details")
public class PayrollDetail {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "teacher_payroll_id")
    private TeacherPayroll payroll;

    @ManyToOne 
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private Integer sessionsTaught;
    private BigDecimal rateApplied;
    private BigDecimal subtotal;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TeacherPayroll getPayroll() {
        return payroll;
    }
    public void setPayroll(TeacherPayroll payroll) {
        this.payroll = payroll;
    }
    public Classroom getClassroom() {
        return classroom;
    }
    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
    public Integer getSessionsTaught() {
        return sessionsTaught;
    }
    public void setSessionsTaught(Integer sessionsTaught) {
        this.sessionsTaught = sessionsTaught;
    }
    public BigDecimal getRateApplied() {
        return rateApplied;
    }
    public void setRateApplied(BigDecimal rateApplied) {
        this.rateApplied = rateApplied;
    }
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    


}
