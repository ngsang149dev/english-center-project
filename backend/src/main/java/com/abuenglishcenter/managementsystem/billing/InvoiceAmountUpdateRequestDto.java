package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class InvoiceAmountUpdateRequestDto {
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
