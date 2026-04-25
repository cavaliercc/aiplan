package com.shift.management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleAssignmentDTO {

    private Integer id;
    private Integer scheduleId;
    private Integer employeeId;
    private String employeeName;
    private LocalDateTime checkedIn;
    private LocalDateTime checkedOut;
    private String status;
}
