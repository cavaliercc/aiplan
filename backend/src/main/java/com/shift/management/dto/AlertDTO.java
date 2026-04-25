package com.shift.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {

    private String type;
    private String title;
    private String content;
    private Integer relatedId;
}
