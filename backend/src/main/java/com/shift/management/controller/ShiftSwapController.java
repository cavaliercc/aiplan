package com.shift.management.controller;

import com.shift.management.common.Result;
import com.shift.management.dto.ShiftSwapRequestDTO;
import com.shift.management.service.ShiftSwapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "换班申请", description = "发起和审批换班请求")
@RestController
@RequestMapping("/api/swap-requests")
@RequiredArgsConstructor
public class ShiftSwapController {

    private final ShiftSwapService shiftSwapService;

    @Operation(summary = "发起换班申请")
    @PostMapping
    public Result<ShiftSwapRequestDTO> create(@RequestBody ShiftSwapRequestDTO dto) {
        return Result.success(shiftSwapService.createRequest(dto));
    }

    @Operation(summary = "审批通过换班申请")
    @PutMapping("/{id}/approve")
    public Result<ShiftSwapRequestDTO> approve(@PathVariable Integer id,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        return Result.success(shiftSwapService.approveRequest(id, userDetails.getUsername()));
    }

    @Operation(summary = "拒绝换班申请")
    @PutMapping("/{id}/reject")
    public Result<ShiftSwapRequestDTO> reject(@PathVariable Integer id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        return Result.success(shiftSwapService.rejectRequest(id, userDetails.getUsername()));
    }
}
