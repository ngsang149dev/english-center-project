package com.abuenglishcenter.managementsystem.billing;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentResponseDto> getAllPayments() {
        return paymentService.getAllPayments();
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
