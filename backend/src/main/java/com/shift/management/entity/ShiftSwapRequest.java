package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("shift_swap_requests")
public class ShiftSwapRequest {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("requester_id")
    private Integer requesterId;

    @TableField("target_id")
    private Integer targetId;

    @TableField("requester_sched_id")
    private Integer requesterSchedId;

    @TableField("target_sched_id")
    private Integer targetSchedId;

    private String reason;

    private String status;

    @TableField("reviewed_by")
    private Integer reviewedBy;

    @TableField("reviewed_at")
    private LocalDateTime reviewedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
