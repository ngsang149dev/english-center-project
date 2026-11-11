package com.abuenglishcenter.managementsystem.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abuenglishcenter.managementsystem.classroom.ClassFeeHistoryRepository;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;
import com.abuenglishcenter.managementsystem.student.StudentRepository;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ClassFeeHistoryRepository classFeeHistoryRepository;

    @Mock
    private StudentRepository studentRepository;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(invoiceRepository, enrollmentRepository, paymentRepository,
                classFeeHistoryRepository, studentRepository);
    }

    @Test
    void remainingAmountIsAdjustedAmountMinusPaidPayments() {
        Invoice invoice = invoice("1000000.00", "1000000.00");
        when(paymentRepository.sumValidByInvoice(1L)).thenReturn(new BigDecimal("600000.00"));

        assertThat(invoiceService.getRemainingAmount(invoice)).isEqualByComparingTo("400000.00");
    }

    @Test
    void fallsBackToBilledAmountWhenAdjustedAmountIsMissing() {
        Invoice invoice = invoice("500000.00", null);
        when(paymentRepository.sumValidByInvoice(1L)).thenReturn(new BigDecimal("100000.00"));

        assertThat(invoiceService.getRemainingAmount(invoice)).isEqualByComparingTo("400000.00");
    }

    @Test
    void overpaidInvoiceNeverReportsNegativeRemaining() {
        Invoice invoice = invoice("1000000.00", "1000000.00");
        when(paymentRepository.sumValidByInvoice(1L)).thenReturn(new BigDecimal("1200000.00"));

        assertThat(invoiceService.getRemainingAmount(invoice)).isEqualByComparingTo("0.00");
    }

    private Invoice invoice(String amount, String adjustedAmount) {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(new BigDecimal(amount));
        invoice.setAdjustedAmount(adjustedAmount == null ? null : new BigDecimal(adjustedAmount));
        return invoice;
    }
}