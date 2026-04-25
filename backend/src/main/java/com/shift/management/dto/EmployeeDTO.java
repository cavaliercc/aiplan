package com.shift.management.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {

    private Integer id;
    private String empNo;
    private String name;
    private String avatarUrl;
    private String phone;
    private String storeId;
    private String storeName;
    private Integer positionId;
    private String positionName;
    private LocalDate hireDate;
    private String status;
}
