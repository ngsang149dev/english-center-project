package com.abuenglishcenter.managementsystem.student;

public enum StudentStatus {
    ACTIVE, INACTIVE, ALUMNI;

    public boolean canMoveTo(StudentStatus target) {
        return switch (this) {
            case ACTIVE -> target == INACTIVE || target == ALUMNI;
            case INACTIVE -> target == ACTIVE || target == ALUMNI;
            case ALUMNI -> false;
        };
    }
}
