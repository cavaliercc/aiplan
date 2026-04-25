package com.shift.management.controller;

import com.shift.management.common.PageResult;
import com.shift.management.common.Result;
import com.shift.management.dto.EmployeeDTO;
import com.shift.management.dto.PreferenceDTO;
import com.shift.management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "员工管理", description = "员工的增删改查及偏好设置")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "获取员工列表（分页+筛选）")
    @GetMapping
    public Result<PageResult<EmployeeDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String storeId) {
        return Result.success(employeeService.getEmployees(page, size, name, storeId));
    }

    @Operation(summary = "获取员工详情")
    @GetMapping("/{id}")
    public Result<EmployeeDTO> getById(@PathVariable Integer id) {
        return Result.success(employeeService.getById(id));
    }

    @Operation(summary = "创建员工")
    @PostMapping
    public Result<EmployeeDTO> create(@RequestBody EmployeeDTO dto) {
        return Result.success(employeeService.create(dto));
    }

    @Operation(summary = "更新员工")
    @PutMapping("/{id}")
    public Result<EmployeeDTO> update(@PathVariable Integer id, @RequestBody EmployeeDTO dto) {
        return Result.success(employeeService.update(id, dto));
    }

    @Operation(summary = "删除员工")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        employeeService.delete(id);
        return Result.success();
    }

    @Operation(summary = "获取员工排班偏好")
    @GetMapping("/{id}/preferences")
    public Result<PreferenceDTO> getPreferences(@PathVariable Integer id) {
        return Result.success(employeeService.getPreferences(id));
    }

    @Operation(summary = "更新员工排班偏好")
    @PutMapping("/{id}/preferences")
    public Result<PreferenceDTO> updatePreferences(@PathVariable Integer id, @RequestBody PreferenceDTO dto) {
        return Result.success(employeeService.updatePreferences(id, dto));
    }
}
