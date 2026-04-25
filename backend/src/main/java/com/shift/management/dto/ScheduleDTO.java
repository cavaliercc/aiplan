package com.shift.management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleDTO {

    private Integer id;
    private String storeId;
    private String storeName;
    private LocalDate shiftDate;
    private Integer shiftTypeId;
    private String shiftTypeName;
    private String zone;
    private Integer vehicleId;
    private String status;
    private Integer createdBy;
    private List<ScheduleAssignmentDTO> assignments;
}
