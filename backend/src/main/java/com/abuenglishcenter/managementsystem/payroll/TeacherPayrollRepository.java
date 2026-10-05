package com.abuenglishcenter.managementsystem.payroll;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherPayrollRepository extends JpaRepository<TeacherPayroll, Long> {
    boolean existsByTeacherIdAndMonthAndYear(Long teacherId, Integer month, Integer year);
    Optional<TeacherPayroll> findByTeacherIdAndMonthAndYear(Long teacherId, Integer month, Integer year);
}
