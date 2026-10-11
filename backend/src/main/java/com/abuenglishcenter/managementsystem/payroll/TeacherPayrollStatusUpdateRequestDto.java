package com.abuenglishcenter.managementsystem.payroll;

import jakarta.validation.constraints.NotNull;

public class TeacherPayrollStatusUpdateRequestDto {
    @NotNull
    private PayrollStatus status;

    public PayrollStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollStatus status) {
        this.status = status;
    }
}
