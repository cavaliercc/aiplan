package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("stores")
public class Store {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;

    private String city;

    private String province;

    private String address;

    private String phone;

    @TableField("open_time")
    private LocalTime openTime;

    @TableField("close_time")
    private LocalTime closeTime;

    private String status;

    @TableField("manager_id")
    private Integer managerId;

    @TableField("resume_date")
    private LocalDate resumeDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
