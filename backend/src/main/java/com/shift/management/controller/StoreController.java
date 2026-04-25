package com.shift.management.controller;

import com.shift.management.common.PageResult;
import com.shift.management.common.Result;
import com.shift.management.dto.StoreDTO;
import com.shift.management.service.StoreService;
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

@Tag(name = "门店管理", description = "门店的增删改查")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "获取门店列表（分页+筛选）")
    @GetMapping
    public Result<PageResult<StoreDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        return Result.success(storeService.getStores(page, size, name, status));
    }

    @Operation(summary = "获取门店详情")
    @GetMapping("/{id}")
    public Result<StoreDTO> getById(@PathVariable String id) {
        return Result.success(storeService.getById(id));
    }

    @Operation(summary = "创建门店")
    @PostMapping
    public Result<StoreDTO> create(@RequestBody StoreDTO dto) {
        return Result.success(storeService.create(dto));
    }

    @Operation(summary = "更新门店")
    @PutMapping("/{id}")
    public Result<StoreDTO> update(@PathVariable String id, @RequestBody StoreDTO dto) {
        return Result.success(storeService.update(id, dto));
    }

    @Operation(summary = "删除门店")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        storeService.delete(id);
        return Result.success();
    }
}
