package com.shift.management.service.impl;

import com.shift.management.dto.AlertDTO;
import com.shift.management.dto.DashboardKpiDTO;
import com.shift.management.dto.TrendDataDTO;
import com.shift.management.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardKpiDTO getKpi() {
        DashboardKpiDTO kpi = new DashboardKpiDTO();
        kpi.setTotalHours(new BigDecimal("2480.5"));
        kpi.setTotalHoursChange(new BigDecimal("5.2"));
        kpi.setLaborCost(new BigDecimal("186037.5"));
        kpi.setLaborCostChange(new BigDecimal("3.8"));
        kpi.setEfficiency(new BigDecimal("87.3"));
        kpi.setEfficiencyChange(new BigDecimal("1.5"));
        kpi.setAttendanceRate(new BigDecimal("96.8"));
        kpi.setAttendanceRateChange(new BigDecimal("0.7"));
        return kpi;
    }

    @Override
    public List<TrendDataDTO> getTrend() {
        List<TrendDataDTO> trend = new ArrayList<>();
        LocalDate base = LocalDate.now().minusDays(6);
        double[] estimated = {320.0, 298.0, 345.0, 312.0, 368.0, 291.0, 356.0};
        double[] actual    = {315.0, 302.0, 338.0, 325.0, 355.0, 285.0, 360.0};
        for (int i = 0; i < 7; i++) {
            TrendDataDTO dto = new TrendDataDTO();
            dto.setDate(base.plusDays(i).toString());
            dto.setEstimatedHours(BigDecimal.valueOf(estimated[i]));
            dto.setActualHours(BigDecimal.valueOf(actual[i]));
            trend.add(dto);
        }
        return trend;
    }

    @Override
    public List<AlertDTO> getAlerts() {
        List<AlertDTO> alerts = new ArrayList<>();

        AlertDTO a1 = new AlertDTO();
        a1.setType("WARNING");
        a1.setTitle("人员短缺预警");
        a1.setContent("明日早班人员不足，建议补充2名员工");
        a1.setRelatedId(null);
        alerts.add(a1);

        AlertDTO a2 = new AlertDTO();
        a2.setType("INFO");
        a2.setTitle("换班申请待审批");
        a2.setContent("有3条换班申请待处理");
        a2.setRelatedId(null);
        alerts.add(a2);

        AlertDTO a3 = new AlertDTO();
        a3.setType("SUCCESS");
        a3.setTitle("本周排班已完成");
        a3.setContent("本周所有门店排班已排满");
        a3.setRelatedId(null);
        alerts.add(a3);

        return alerts;
    }
}
