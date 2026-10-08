package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
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
    public InvoiceGenerationResultDto generateInvoices(@RequestParam Integer month, @RequestParam Integer year) {
        return invoiceService.generateMonthlyInvoices(month, year);
    }

    @PutMapping("/{id}/amount")
    public InvoiceResponseDto updateAdjustedAmount(@PathVariable Long id, @RequestBody BigDecimal amount) {
        return invoiceService.updateAdjustedAmount(id, amount);
    }

}
