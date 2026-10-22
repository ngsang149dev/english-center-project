package com.abuenglishcenter.managementsystem.enrollment;

public enum EnrollmentStatus {
    ACTIVE, WITHDRAWN, COMPLETED;

    public boolean canMoveTo(EnrollmentStatus target) {
        return this == ACTIVE && (target == WITHDRAWN || target == COMPLETED);
    }
}
