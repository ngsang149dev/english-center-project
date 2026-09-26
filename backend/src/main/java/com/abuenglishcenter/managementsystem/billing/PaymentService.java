package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class PaymentService {

    @Autowired 
    private PaymentRepository paymentRepository;

    @Autowired 
    private InvoiceRepository invoiceRepository;

    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional 
    public PaymentResponseDto createPayment(PaymentCreateRequestDto request) {
        Invoice checkInvoice = invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() -> new RuntimeException("Invoice not found"));

        Payment newPayment = new Payment();
        newPayment.setInvoice(checkInvoice);
        newPayment.setAmount(request.getAmount());
        newPayment.setPaymentDate(request.getPaymentDate());
        newPayment.setPaymentMethod(request.getPaymentMethod());
        Payment saved = paymentRepository.save(newPayment);

        recalculateInvoiceStatus(checkInvoice);
        return toDto(saved);
    }

    public PaymentResponseDto cancelPayment(Long paymentId) {
        Payment checkPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        checkPayment.setCancelled(true);
        Payment updated = paymentRepository.save(checkPayment);
        recalculateInvoiceStatus(checkPayment.getInvoice());
        return toDto(updated);
    }

    private void recalculateInvoiceStatus(Invoice invoice) {
        List<Payment> validPayments = paymentRepository.findByInvoiceIdAndCancelledFalse(invoice.getId());
        
        BigDecimal totalPaid = validPayments.stream().map(payment -> payment.getAmount()).reduce(BigDecimal.ZERO, (total, amount) -> total.add(amount));
        if (totalPaid.compareTo(invoice.getAdjustedAmount()) >= 0) {
            invoice.setStatus(Status.PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(Status.PARTIALLY_PAID);
        } else {
            invoice.setStatus(Status.UNPAID);
        }
        invoiceRepository.save(invoice);
    }

    private PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getInvoice().getId(), payment.getAmount(), payment.getPaymentDate(), payment.getPaymentMethod(), payment.isCancelled());
    }
}
