package com.shift.management.controller;

import com.shift.management.common.Result;
import com.shift.management.entity.Notification;
import com.shift.management.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "通知管理", description = "查看和标记通知")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "获取当前用户通知列表")
    @GetMapping
    public Result<List<Notification>> list(@AuthenticationPrincipal UserDetails userDetails) {
        return Result.success(notificationService.getByUsername(userDetails.getUsername()));
    }

    @Operation(summary = "标记通知为已读")
    @PutMapping("/{id}/read")
    public Result<Void> markRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return Result.success();
    }
}
