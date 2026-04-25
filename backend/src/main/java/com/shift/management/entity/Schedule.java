package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("schedules")
public class Schedule {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("store_id")
    private String storeId;

    @TableField("shift_date")
    private LocalDate shiftDate;

    @TableField("shift_type_id")
    private Integer shiftTypeId;

    private String zone;

    @TableField("vehicle_id")
    private Integer vehicleId;

    private String status;

    @TableField("created_by")
    private Integer createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
