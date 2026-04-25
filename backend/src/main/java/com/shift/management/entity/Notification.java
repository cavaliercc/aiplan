package com.shift.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notifications")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String type;

    private String title;

    private String content;

    @TableField("related_id")
    private Integer relatedId;

    @TableField("related_type")
    private String relatedType;

    @TableField("target_user")
    private Integer targetUser;

    @TableField("is_read")
    private Boolean isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
