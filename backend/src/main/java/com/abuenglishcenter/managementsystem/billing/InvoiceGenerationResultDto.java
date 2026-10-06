package com.abuenglishcenter.managementsystem.billing;

import java.util.List;

public class InvoiceGenerationResultDto {
    private Integer month;
    private Integer year;
    private int createdCount;
    private int alreadyExistedCount;
    private int missingFeeCount;
    private List<String> classesWithoutFee;

    public InvoiceGenerationResultDto(Integer month, Integer year, int createdCount, int alreadyExistedCount,
            int missingFeeCount, List<String> classesWithoutFee) {
        this.month = month;
        this.year = year;
        this.createdCount = createdCount;
        this.alreadyExistedCount = alreadyExistedCount;
        this.missingFeeCount = missingFeeCount;
        this.classesWithoutFee = classesWithoutFee;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public int getCreatedCount() {
        return createdCount;
    }

    public int getAlreadyExistedCount() {
        return alreadyExistedCount;
    }

    public int getMissingFeeCount() {
        return missingFeeCount;
    }

    public List<String> getClassesWithoutFee() {
        return classesWithoutFee;
    }

    
}
