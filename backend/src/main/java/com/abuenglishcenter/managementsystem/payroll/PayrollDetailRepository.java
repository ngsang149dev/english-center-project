package com.abuenglishcenter.managementsystem.payroll;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Long>{
    List<PayrollDetail> findByPayrollId(Long payrollId);
    void deleteByPayrollId(Long payrollId);
}
