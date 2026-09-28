package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.enrollment.Enrollment;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;

@Service 
public class InvoiceService {

    @Autowired 
    private InvoiceRepository invoiceRepository;

    @Autowired 
    private EnrollmentRepository enrollmentRepository;

    @Autowired 
    private PaymentRepository paymentRepository;

    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::toDto).toList();
    }

    public InvoiceResponseDto createInvoice(InvoiceCreateRequestDto request) {
        Enrollment checkEnrollment = enrollmentRepository.findById(request.getEnrollmentId()).orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Invoice newInvoice = new Invoice();
        newInvoice.setEnrollment(checkEnrollment);
        newInvoice.setMonth(request.getMonth());
        newInvoice.setYear(request.getYear());
        newInvoice.setAmount(request.getAmount());
        newInvoice.setAdjustedAmount(request.getAmount());
        newInvoice.setStatus(Status.UNPAID);
        
        Invoice saved = invoiceRepository.save(newInvoice);
        return toDto(saved);
    }

    @Transactional 
    public InvoiceResponseDto updateAdjustedAmount(Long id, BigDecimal newAmount) {
        Invoice checkInvoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found"));

        checkInvoice.setAdjustedAmount(newAmount);
        recalculateInvoiceStatus(checkInvoice);
        return toDto(checkInvoice);
    }

    public  void recalculateInvoiceStatus(Invoice invoice) {
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

    private InvoiceResponseDto toDto(Invoice invoice) {
        return new InvoiceResponseDto(invoice.getId(), invoice.getEnrollment().getId(), invoice.getMonth(), invoice.getYear(), invoice.getAmount(), invoice.getAdjustedAmount(), invoice.getStatus());
    }
}
