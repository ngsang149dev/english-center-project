package com.abuenglishcenter.managementsystem.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceService invoiceService;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, invoiceRepository, invoiceService);
    }

    @Test
    void rejectsPaymentOneCentOverTheRemainingBalance() {
        Invoice invoice = invoice(10L);
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(invoice));
        when(invoiceService.getRemainingAmount(invoice)).thenReturn(new BigDecimal("400000.00"));

        assertThatThrownBy(() -> paymentService.createPayment(request(10L, "400000.01")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Payment amount 400000.01 exceeds the remaining balance 400000.00 of invoice 10.");

        verify(paymentRepository, never()).save(any());
        verify(invoiceService, never()).recalculateInvoiceStatus(any());
    }

    @Test
    void rejectsAnyPaymentWhenInvoiceIsFullyPaid() {
        Invoice invoice = invoice(10L);
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(invoice));
        when(invoiceService.getRemainingAmount(invoice)).thenReturn(new BigDecimal("0.00"));

        assertThatThrownBy(() -> paymentService.createPayment(request(10L, "1")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Payment amount 1 exceeds the remaining balance 0.00 of invoice 10.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void acceptsPaymentEqualToTheRemainingBalance() {
        Invoice invoice = invoice(10L);
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(invoice));
        when(invoiceService.getRemainingAmount(invoice)).thenReturn(new BigDecimal("400000.00"));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(call -> {
            Payment payment = call.getArgument(0);
            payment.setId(7L);
            return payment;
        });

        PaymentResponseDto response = paymentService.createPayment(request(10L, "400000.00"));

        assertThat(response.getId()).isEqualTo(7L);
        assertThat(response.getInvoiceId()).isEqualTo(10L);
        assertThat(response.getAmount()).isEqualByComparingTo("400000.00");
        assertThat(response.isCancelled()).isFalse();
        verify(invoiceService).recalculateInvoiceStatus(invoice);
    }

    @Test
    void unknownInvoiceIsNotFound() {
        when(invoiceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.createPayment(request(99L, "1000")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Invoice with id 99 not found");

        verify(paymentRepository, never()).save(any());
    }

    private Invoice invoice(Long id) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setAmount(new BigDecimal("1000000.00"));
        invoice.setAdjustedAmount(new BigDecimal("1000000.00"));
        return invoice;
    }

    private PaymentCreateRequestDto request(Long invoiceId, String amount) {
        PaymentCreateRequestDto request = new PaymentCreateRequestDto();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal(amount));
        request.setPaymentDate(LocalDate.of(2026, 11, 11));
        request.setPaymentMethod(PaymentMethod.CASH);
        return request;
    }
}