package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class TeacherPayrollBonusUpdateRequestDto {
    @NotNull
    @PositiveOrZero
    private BigDecimal bonus;

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }
}
