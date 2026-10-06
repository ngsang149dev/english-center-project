package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherPayrollRepository extends JpaRepository<TeacherPayroll, Long> {
    boolean existsByTeacherIdAndMonthAndYear(Long teacherId, Integer month, Integer year);

    Optional<TeacherPayroll> findByTeacherIdAndMonthAndYear(Long teacherId, Integer month, Integer year);

    @Query("SELECT COALESCE(SUM(p.totalPay), 0) FROM TeacherPayroll p " +
       "WHERE p.status = :status AND p.paidDate >= :startDate AND p.paidDate <= :endDate")
    BigDecimal sumPaidBetween(@Param("status") PayrollStatus status,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);
}
