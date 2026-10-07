package com.abuenglishcenter.managementsystem.billing;

import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component 
public class InvoiceScheduler {
    private static final Logger log = LoggerFactory.getLogger(InvoiceScheduler.class);
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Autowired 
    private InvoiceService invoiceService;

    @Scheduled(cron = "${app.invoice.generation-cron}", zone = "Asia/Ho_Chi_Minh")
    public void generateInvoicesForCurrentMonth() {
        LocalDate today = LocalDate.now(ZONE);

        try {
            InvoiceGenerationResultDto result = invoiceService.generateMonthlyInvoices(today.getMonthValue(), today.getYear());
            log.info("Invoices {}/{}: created={}, alreadyExisted={}, missingFee={}", 
                            today.getMonthValue(), today.getYear(), result.getCreatedCount(), result.getAlreadyExistedCount(), result.getMissingFeeCount()
            );
            if (result.getMissingFeeCount() > 0) {
                log.warn("Classes without tuition fee: {}", result.getClassesWithoutFee());
            }
        } catch (Exception ex) {
            log.error("Monthly invoice generation false", ex);
        }
    }
}
