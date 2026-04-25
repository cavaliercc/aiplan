package com.shift.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataDTO {

    private String date;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
}
