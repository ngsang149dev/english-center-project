package com.abuenglishcenter.managementsystem.billing;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<InvoiceResponseDto> getInvoices(@RequestParam(required = false) Long studentId) {
        return studentId == null ? invoiceService.getAllInvoices() : invoiceService.getInvoicesByStudent(studentId);
    }

    @GetMapping("/balance")
    public StudentBalanceResponseDto getStudentBalance(@RequestParam Long studentId) {
        return invoiceService.getStudentBalance(studentId);
    }

    @PostMapping
    public InvoiceResponseDto createInvoice(@Valid @RequestBody InvoiceCreateRequestDto request) {
        return invoiceService.createInvoice(request);
    }

    @PostMapping("/generate")
    public InvoiceGenerationResultDto generateInvoices(@RequestParam @Min(1) @Max(12) Integer month, @RequestParam @Min(2000) @Max(2100) Integer year) {
        return invoiceService.generateMonthlyInvoices(month, year);
    }

    @PutMapping("/{id}/amount")
    public InvoiceResponseDto updateAdjustedAmount(@PathVariable Long id,
            @Valid @RequestBody InvoiceAmountUpdateRequestDto request) {
        return invoiceService.updateAdjustedAmount(id, request.getAmount());
    }

}
