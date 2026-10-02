package com.abuenglishcenter.managementsystem.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherPayrollRepository extends JpaRepository<TeacherPayroll, Long> {
    boolean existsByTeacherIdAndMonthAndYear(Long teacherId, Integer month, Integer year);
}
