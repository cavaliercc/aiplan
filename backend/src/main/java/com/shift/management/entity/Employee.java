package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("employees")
public class Employee {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("emp_no")
    private String empNo;

    private String name;

    @TableField("avatar_url")
    private String avatarUrl;

    private String phone;

    @TableField("store_id")
    private String storeId;

    @TableField("position_id")
    private Integer positionId;

    @TableField("hire_date")
    private LocalDate hireDate;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
