package com.abuenglishcenter.managementsystem.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@RestController 
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public MonthlyReportResponseDto getMonthlyReport(
        @RequestParam @Min(2000) @Max(2100) int year, @RequestParam @Min(1) @Max(12)  int month) {
            return reportService.getMonthlyReport(year, month);  
        }
}
