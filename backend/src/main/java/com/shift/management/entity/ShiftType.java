package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("shift_types")
public class ShiftType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;

    @TableField("store_id")
    private String storeId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
