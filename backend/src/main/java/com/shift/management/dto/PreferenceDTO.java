package com.shift.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class PreferenceDTO {

    private Integer id;
    private Integer employeeId;
    private Integer preferredShiftId;
    private String preferredShiftName;
    private Integer maxDailyHours;
    private List<Integer> restDays;
    private List<UnavailabilityBlockDTO> unavailabilityBlocks;

    @Data
    public static class UnavailabilityBlockDTO {
        private Integer id;
        private String date;
        private String startTime;
        private String endTime;
        private String reason;
    }
}
