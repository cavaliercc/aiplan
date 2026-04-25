package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("positions")
public class Position {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer level;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
