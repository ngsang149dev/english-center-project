package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceIdAndCancelledFalse(Long invoiceId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
       "WHERE p.cancelled = false " +
       "AND p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    BigDecimal sumValidPaymentsBetween(@Param("startDate")LocalDate startDate,@Param("endDate") LocalDate endDate);
}
