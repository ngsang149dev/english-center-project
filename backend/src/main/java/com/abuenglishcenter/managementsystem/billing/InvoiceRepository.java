package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByEnrollmentIdAndMonthAndYear(Long enrollmentId, Integer month, Integer year);

    List<Invoice> findByEnrollmentStudentId(Long studentId);

    @Query("SELECT COALESCE(SUM(i.adjustedAmount), 0) FROM Invoice i WHERE i.enrollment.student.id = :studentId")
    BigDecimal sumBilledByStudent(@Param("studentId") Long studentId);
}
