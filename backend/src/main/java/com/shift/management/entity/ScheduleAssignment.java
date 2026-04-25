package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("schedule_assignments")
public class ScheduleAssignment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("schedule_id")
    private Integer scheduleId;

    @TableField("employee_id")
    private Integer employeeId;

    @TableField("checked_in")
    private LocalDateTime checkedIn;

    @TableField("checked_out")
    private LocalDateTime checkedOut;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
