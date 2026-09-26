package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentCreateRequestDto {
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDate paymentDate;
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
