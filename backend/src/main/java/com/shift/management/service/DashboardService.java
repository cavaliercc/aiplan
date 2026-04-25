package com.shift.management.service;

import com.shift.management.dto.AlertDTO;
import com.shift.management.dto.DashboardKpiDTO;
import com.shift.management.dto.TrendDataDTO;

import java.util.List;

public interface DashboardService {
    DashboardKpiDTO getKpi();
    List<TrendDataDTO> getTrend();
    List<AlertDTO> getAlerts();
}
