package com.abuenglishcenter.managementsystem.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.ClassFeeHistory;
import com.abuenglishcenter.managementsystem.classroom.ClassFeeHistoryRepository;
import com.abuenglishcenter.managementsystem.enrollment.Enrollment;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentStatus;
import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;
import com.abuenglishcenter.managementsystem.student.StudentRepository;

@Service
public class InvoiceService {

    @Value("${app.invoice.pro-rate-first-month:true}")
    private boolean proRateFirstMonth;

    private final InvoiceRepository invoiceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final ClassFeeHistoryRepository classFeeHistoryRepository;
    private final StudentRepository studentRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, EnrollmentRepository enrollmentRepository,
            PaymentRepository paymentRepository, ClassFeeHistoryRepository classFeeHistoryRepository,
            StudentRepository studentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
        this.classFeeHistoryRepository = classFeeHistoryRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public InvoiceGenerationResultDto generateMonthlyInvoices(Integer month, Integer year) {
        validateMonthYear(month, year);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<Enrollment> enrollments = enrollmentRepository
                .findByStatusAndEnrolledDateLessThanEqual(EnrollmentStatus.ACTIVE, lastDay);

        Map<Long, Optional<BigDecimal>> feeCache = new HashMap<>();
        Set<String> classesWithoutFee = new LinkedHashSet<>();

        int created = 0, alreadyExisted = 0, missingFee = 0;

        for (Enrollment e : enrollments) {

            if (invoiceRepository.existsByEnrollmentIdAndMonthAndYear(e.getId(), month, year)) {
                alreadyExisted++;
                continue;
            }

            Optional<BigDecimal> fee = feeCache.computeIfAbsent(e.getClassroom().getId(),
                    classId -> classFeeHistoryRepository.findEffectiveFee(classId, firstDay)
                            .map(ClassFeeHistory::getMonthlyFee));

            if (fee.isEmpty()) {
                missingFee++;
                classesWithoutFee.add(e.getClassroom().getName());
                continue;
            }

            invoiceRepository.save(buildInvoice(e, month, year, fee.get(), calculateAdjustedAmount(e, fee.get(), month, year)));
            created++;
        }

        return new InvoiceGenerationResultDto(month, year, created, alreadyExisted, missingFee, new ArrayList<>(classesWithoutFee));
    }

    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::toDto).toList();
    }

    private BigDecimal calculateAdjustedAmount(Enrollment enrollment, BigDecimal fee, Integer month, Integer year) {
        LocalDate enrolled = enrollment.getEnrolledDate();
        boolean startMidMonth = proRateFirstMonth && enrolled.getYear() == year && enrolled.getMonthValue() == month && enrolled.getDayOfMonth() > 1;

        if (!startMidMonth) {
            return fee;
        }

        int daysInMonth = enrolled.lengthOfMonth();
        int remainingDays = daysInMonth - enrolled.getDayOfMonth() + 1;

        return fee.multiply(BigDecimal.valueOf(remainingDays)).divide(BigDecimal.valueOf(daysInMonth), 0, RoundingMode.HALF_UP);
    }

    private void validateMonthYear(Integer month, Integer year) {
        if (month == null || month < 1 || month > 12 || year == null) {
            throw new BusinessRuleException("Month must be between 1 and 12 and year is required.");
        }
    }

    private Invoice buildInvoice(Enrollment enrollment, Integer month, Integer year, BigDecimal amount, BigDecimal adjustedAmount) {
        Invoice invoice = new Invoice();
        invoice.setEnrollment(enrollment);
        invoice.setMonth(month);
        invoice.setYear(year);
        invoice.setAmount(amount);
        invoice.setAdjustedAmount(adjustedAmount);
        invoice.setStatus(InvoiceStatus.UNPAID);
        return invoice;
    }

    @Transactional
    public InvoiceResponseDto createInvoice(InvoiceCreateRequestDto request) {
        validateMonthYear(request.getMonth(), request.getYear());

        if (request.getAmount() != null && request.getAmount().signum() <= 0) {
            throw new BusinessRuleException("Amount must be positive.");
        }

        Enrollment checkEnrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new NotFoundException(
                        "Enrollment with id " + request.getEnrollmentId() + " not found"));

        if (invoiceRepository.existsByEnrollmentIdAndMonthAndYear(request.getEnrollmentId(), request.getMonth(),
                request.getYear())) {
            throw new BusinessRuleException(
                    "Enrollment " + checkEnrollment.getId()
                            + " already has an invoice for " + request.getMonth() + "/" + request.getYear() + ".");
        }

        BigDecimal amount = request.getAmount();
        BigDecimal adjustedAmount = amount;

        if (amount == null) {
            LocalDate firstDay = LocalDate.of(request.getYear(), request.getMonth(), 1);
            amount = classFeeHistoryRepository.findEffectiveFee(checkEnrollment.getClassroom().getId(), firstDay)
                    .orElseThrow(() -> new BusinessRuleException(
                            "Class " + checkEnrollment.getClassroom().getName()
                                    + " has no tuition fee for " + request.getMonth() + "/" + request.getYear()))
                    .getMonthlyFee();
            adjustedAmount = calculateAdjustedAmount(checkEnrollment, amount, request.getMonth(), request.getYear());
        }

        Invoice saved = invoiceRepository
                .save(buildInvoice(checkEnrollment, request.getMonth(), request.getYear(), amount, adjustedAmount));
        return toDto(saved);
    }

    @Transactional
    public InvoiceResponseDto updateAdjustedAmount(Long id, BigDecimal newAmount) {
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("New amount can not be negative");
        }
        Invoice checkInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice with id " + id + " not found"));

        checkInvoice.setAdjustedAmount(newAmount);
        recalculateInvoiceStatus(checkInvoice);
        return toDto(checkInvoice);
    }

    public void recalculateInvoiceStatus(Invoice invoice) {
        List<Payment> validPayments = paymentRepository.findByInvoiceIdAndCancelledFalse(invoice.getId());

        BigDecimal totalPaid = validPayments.stream().map(payment -> payment.getAmount()).reduce(BigDecimal.ZERO,
                (total, amount) -> total.add(amount));
        if (totalPaid.compareTo(invoice.getAdjustedAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setStatus(InvoiceStatus.UNPAID);
        }
        invoiceRepository.save(invoice);
    }

    public List<InvoiceResponseDto> getInvoicesByStudent(Long studentId) {
        ensureStudentExists(studentId);

        return invoiceRepository.findByEnrollmentStudentId(studentId).stream().map(this::toDto).toList();
    }

    public StudentBalanceResponseDto getStudentBalance(Long studentId) {
        ensureStudentExists(studentId);

        BigDecimal billed = invoiceRepository.sumBilledByStudent(studentId);
        BigDecimal paid = paymentRepository.sumValidByStudent(studentId);
        return new StudentBalanceResponseDto(studentId, billed, paid);
    }

    private void ensureStudentExists(Long studentId) {
        if (!studentRepository.existsById(studentId))
            throw new NotFoundException("Student with id " + studentId + " not found");
    }

    private InvoiceResponseDto toDto(Invoice invoice) {
        BigDecimal paid = paymentRepository.sumValidByInvoice(invoice.getId());
        BigDecimal remaining = invoice.getAdjustedAmount().subtract(paid);
        Enrollment enrollment = invoice.getEnrollment();
        return new InvoiceResponseDto(
                invoice.getId(),
                enrollment.getId(),
                invoice.getMonth(),
                invoice.getYear(),
                invoice.getAmount(),
                invoice.getAdjustedAmount(),
                invoice.getStatus(),
                paid,
                remaining.max(BigDecimal.ZERO),
                enrollment.getStudent().getUser().getFullName(),
                enrollment.getClassroom().getName());
    }
}
