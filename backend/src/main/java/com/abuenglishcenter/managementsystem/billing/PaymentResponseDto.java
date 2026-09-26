package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentResponseDto {
    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private boolean cancelled;

    public PaymentResponseDto(Long id, Long invoiceId, BigDecimal amount, LocalDate paymentDate,
            PaymentMethod paymentMethod, boolean cancelled) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.cancelled = cancelled;
    }

    public Long getId() {
        return id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    public boolean isCancelled() {
        return cancelled;
    }
    
}
