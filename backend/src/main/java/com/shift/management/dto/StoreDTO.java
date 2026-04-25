package com.shift.management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class StoreDTO {

    private String id;
    private String name;
    private String city;
    private String province;
    private String address;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String status;
    private Integer managerId;
    private String managerName;
    private LocalDate resumeDate;
    private Integer employeeCount;
}
