package com.abuenglishcenter.managementsystem.payroll;

public enum PayrollStatus {
    DRAFT, CONFIRMED, PAID;

    public boolean canMoveTo(PayrollStatus target) {
        return switch (this) {
            case DRAFT -> target == CONFIRMED;
            case CONFIRMED -> target == PAID || target == DRAFT;
            case PAID -> target == CONFIRMED;
        };
    }
}
