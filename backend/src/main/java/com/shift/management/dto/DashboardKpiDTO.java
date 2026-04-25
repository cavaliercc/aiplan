package com.shift.management.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardKpiDTO {

    private BigDecimal totalHours;
    private BigDecimal totalHoursChange;
    private BigDecimal laborCost;
    private BigDecimal laborCostChange;
    private BigDecimal efficiency;
    private BigDecimal efficiencyChange;
    private BigDecimal attendanceRate;
    private BigDecimal attendanceRateChange;
}
