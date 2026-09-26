package com.abuenglishcenter.managementsystem.billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired 
    private PaymentService paymentService;

    @GetMapping 
    public List<PaymentResponseDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PostMapping 
    public PaymentResponseDto createPayment(@RequestBody PaymentCreateRequestDto request) {
        return paymentService.createPayment(request);
    }

    @PutMapping("/{id}/cancel")
    public PaymentResponseDto cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }
}
