package com.shift.management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftSwapRequestDTO {

    private Integer id;
    private Integer requesterId;
    private String requesterName;
    private Integer targetId;
    private String targetName;
    private Integer requesterSchedId;
    private Integer targetSchedId;
    private String reason;
    private String status;
    private Integer reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
