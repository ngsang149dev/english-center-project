package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class PaymentCreateRequestDto {
    @NotNull
    @Positive
    private Long invoiceId;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    @PastOrPresent
    private LocalDate paymentDate;
    @NotNull
    private PaymentMethod paymentMethod;
    
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    
}
