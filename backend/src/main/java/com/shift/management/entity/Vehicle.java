package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("vehicles")
public class Vehicle {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("plate_no")
    private String plateNo;

    @TableField("store_id")
    private String storeId;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
