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

    @Autowired 
    private InvoiceService invoiceService;

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

        invoiceService.recalculateInvoiceStatus(checkInvoice);
        return toDto(saved);
    }

    @Transactional 
    public PaymentResponseDto cancelPayment(Long paymentId) {
        Payment checkPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        checkPayment.setCancelled(true);
        Payment updated = paymentRepository.save(checkPayment);
        invoiceService.recalculateInvoiceStatus(checkPayment.getInvoice());
        return toDto(updated);
    }

    

    private PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getInvoice().getId(), payment.getAmount(), payment.getPaymentDate(), payment.getPaymentMethod(), payment.isCancelled());
    }
}
