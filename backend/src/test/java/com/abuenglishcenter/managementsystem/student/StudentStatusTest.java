package com.abuenglishcenter.managementsystem.student;

import static com.abuenglishcenter.managementsystem.student.StudentStatus.ACTIVE;
import static com.abuenglishcenter.managementsystem.student.StudentStatus.ALUMNI;
import static com.abuenglishcenter.managementsystem.student.StudentStatus.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StudentStatusTest {

    @Test
    void activeStudentCanBeDeactivatedOrGraduate() {
        assertThat(ACTIVE.canMoveTo(INACTIVE)).isTrue();
        assertThat(ACTIVE.canMoveTo(ALUMNI)).isTrue();
        assertThat(ACTIVE.canMoveTo(ACTIVE)).isFalse();
    }

    @Test
    void inactiveStudentCanComeBackOrGraduate() {
        assertThat(INACTIVE.canMoveTo(ACTIVE)).isTrue();
        assertThat(INACTIVE.canMoveTo(ALUMNI)).isTrue();
        assertThat(INACTIVE.canMoveTo(INACTIVE)).isFalse();
    }

    @Test
    void alumnusIsTerminal() {
        assertThat(ALUMNI.canMoveTo(ACTIVE)).isFalse();
        assertThat(ALUMNI.canMoveTo(INACTIVE)).isFalse();
        assertThat(ALUMNI.canMoveTo(ALUMNI)).isFalse();
    }
}