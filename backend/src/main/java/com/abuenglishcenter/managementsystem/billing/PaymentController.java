package com.abuenglishcenter.managementsystem.billing;

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

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public PageResponseDto<PaymentResponseDto> getAllPayments(@RequestParam(defaultValue = "0") @Min(0) @Max(1000) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return paymentService.getAllPayments(page, size);
    }

    @PostMapping
    public PaymentResponseDto createPayment(@Valid @RequestBody PaymentCreateRequestDto request) {
        return paymentService.createPayment(request);
    }

    @PutMapping("/{id}/cancel")
    public PaymentResponseDto cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }
}
