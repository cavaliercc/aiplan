package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("schedule_preferences")
public class SchedulePreference {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("employee_id")
    private Integer employeeId;

    @TableField("preferred_shift_id")
    private Integer preferredShiftId;

    @TableField("max_daily_hours")
    private Integer maxDailyHours;

    @TableField("rest_days")
    private String restDays;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
