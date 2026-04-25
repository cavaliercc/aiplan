package com.shift.management.controller;

import com.shift.management.common.Result;
import com.shift.management.dto.AlertDTO;
import com.shift.management.dto.DashboardKpiDTO;
import com.shift.management.dto.TrendDataDTO;
import com.shift.management.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "仪表盘", description = "KPI指标、趋势图、预警信息")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取KPI指标")
    @GetMapping("/kpi")
    public Result<DashboardKpiDTO> kpi() {
        return Result.success(dashboardService.getKpi());
    }

    @Operation(summary = "获取工时趋势数据")
    @GetMapping("/trend")
    public Result<List<TrendDataDTO>> trend() {
        return Result.success(dashboardService.getTrend());
    }

    @Operation(summary = "获取预警信息")
    @GetMapping("/alerts")
    public Result<List<AlertDTO>> alerts() {
        return Result.success(dashboardService.getAlerts());
    }
}
