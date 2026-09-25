package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.enrollment.Enrollment;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;

@Service 
public class InvoiceService {

    @Autowired 
    private InvoiceRepository invoiceRepository;

    @Autowired 
    private EnrollmentRepository enrollmentRepository;

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

    public InvoiceResponseDto updateAdjustedAmount(Long id, BigDecimal amount) {
        Invoice checkInvoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found"));

        checkInvoice.setAdjustedAmount(amount);

        Invoice updated = invoiceRepository.save(checkInvoice);
        return toDto(updated);
    }

    private InvoiceResponseDto toDto(Invoice invoice) {
        return new InvoiceResponseDto(invoice.getId(), invoice.getEnrollment().getId(), invoice.getMonth(), invoice.getYear(), invoice.getAmount(), invoice.getAdjustedAmount(), invoice.getStatus());
    }
}
