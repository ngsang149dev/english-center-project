package com.abuenglishcenter.managementsystem.enrollment;

import static com.abuenglishcenter.managementsystem.enrollment.EnrollmentStatus.ACTIVE;
import static com.abuenglishcenter.managementsystem.enrollment.EnrollmentStatus.COMPLETED;
import static com.abuenglishcenter.managementsystem.enrollment.EnrollmentStatus.WITHDRAWN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnrollmentStatusTest {

    @Test
    void activeEnrollmentCanWithdrawOrComplete() {
        assertThat(ACTIVE.canMoveTo(WITHDRAWN)).isTrue();
        assertThat(ACTIVE.canMoveTo(COMPLETED)).isTrue();
    }

    @Test
    void activeEnrollmentCannotMoveToItself() {
        assertThat(ACTIVE.canMoveTo(ACTIVE)).isFalse();
    }

    @Test
    void withdrawnEnrollmentIsTerminal() {
        assertThat(WITHDRAWN.canMoveTo(ACTIVE)).isFalse();
        assertThat(WITHDRAWN.canMoveTo(COMPLETED)).isFalse();
        assertThat(WITHDRAWN.canMoveTo(WITHDRAWN)).isFalse();
    }

    @Test
    void completedEnrollmentIsTerminal() {
        assertThat(COMPLETED.canMoveTo(ACTIVE)).isFalse();
        assertThat(COMPLETED.canMoveTo(WITHDRAWN)).isFalse();
        assertThat(COMPLETED.canMoveTo(COMPLETED)).isFalse();
    }
}