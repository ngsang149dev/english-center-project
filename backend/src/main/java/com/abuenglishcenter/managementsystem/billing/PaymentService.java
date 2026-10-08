package com.abuenglishcenter.managementsystem.billing;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.exception.NotFoundException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository,
            InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateRequestDto request) {
        Invoice checkInvoice = invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() -> new NotFoundException("Invoice with id " + request.getInvoiceId() + " not found"));

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
        Payment checkPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new NotFoundException("Payment with id " + paymentId + " not found"));

        checkPayment.setCancelled(true);
        Payment updated = paymentRepository.save(checkPayment);
        invoiceService.recalculateInvoiceStatus(checkPayment.getInvoice());
        return toDto(updated);
    }

    private PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getInvoice().getId(), payment.getAmount(), payment.getPaymentDate(), payment.getPaymentMethod(), payment.isCancelled());
    }
}
