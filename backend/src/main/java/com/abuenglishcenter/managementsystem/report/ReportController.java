package com.abuenglishcenter.managementsystem.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/reports")
public class ReportController {
    @Autowired 
    private ReportService reportService;

    @GetMapping("/monthly")
    public MonthlyReportResponseDto getMonthlyReport(
        @RequestParam int year, @RequestParam  int month) {
            return reportService.getMonthlyReport(year, month);  
        }
}
