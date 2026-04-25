package com.shift.management.controller;

import com.shift.management.common.PageResult;
import com.shift.management.common.Result;
import com.shift.management.dto.ScheduleAssignmentDTO;
import com.shift.management.dto.ScheduleDTO;
import com.shift.management.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "排班管理", description = "排班的增删改查及人员分配")
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "获取排班列表（分页+筛选）")
    @GetMapping
    public Result<PageResult<ScheduleDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(scheduleService.getSchedules(page, size, storeId, date));
    }

    @Operation(summary = "获取排班详情")
    @GetMapping("/{id}")
    public Result<ScheduleDTO> getById(@PathVariable Integer id) {
        return Result.success(scheduleService.getById(id));
    }

    @Operation(summary = "创建排班")
    @PostMapping
    public Result<ScheduleDTO> create(@RequestBody ScheduleDTO dto) {
        return Result.success(scheduleService.create(dto));
    }

    @Operation(summary = "更新排班")
    @PutMapping("/{id}")
    public Result<ScheduleDTO> update(@PathVariable Integer id, @RequestBody ScheduleDTO dto) {
        return Result.success(scheduleService.update(id, dto));
    }

    @Operation(summary = "删除排班")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        scheduleService.delete(id);
        return Result.success();
    }

    @Operation(summary = "添加排班人员")
    @PostMapping("/{id}/assignments")
    public Result<ScheduleAssignmentDTO> addAssignment(@PathVariable Integer id,
                                                        @RequestBody ScheduleAssignmentDTO dto) {
        return Result.success(scheduleService.addAssignment(id, dto));
    }
}
