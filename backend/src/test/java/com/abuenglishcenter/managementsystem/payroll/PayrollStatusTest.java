package com.abuenglishcenter.managementsystem.payroll;

import static com.abuenglishcenter.managementsystem.payroll.PayrollStatus.CONFIRMED;
import static com.abuenglishcenter.managementsystem.payroll.PayrollStatus.DRAFT;
import static com.abuenglishcenter.managementsystem.payroll.PayrollStatus.PAID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PayrollStatusTest {

    @Test
    void draftCanOnlyBeConfirmed() {
        assertThat(DRAFT.canMoveTo(CONFIRMED)).isTrue();
        assertThat(DRAFT.canMoveTo(PAID)).isFalse();
        assertThat(DRAFT.canMoveTo(DRAFT)).isFalse();
    }

    @Test
    void confirmedCanBePaidOrSentBackToDraft() {
        assertThat(CONFIRMED.canMoveTo(PAID)).isTrue();
        assertThat(CONFIRMED.canMoveTo(DRAFT)).isTrue();
        assertThat(CONFIRMED.canMoveTo(CONFIRMED)).isFalse();
    }

    @Test
    void paidCanOnlyBeReopenedToConfirmed() {
        assertThat(PAID.canMoveTo(CONFIRMED)).isTrue();
        assertThat(PAID.canMoveTo(DRAFT)).isFalse();
        assertThat(PAID.canMoveTo(PAID)).isFalse();
    }
}