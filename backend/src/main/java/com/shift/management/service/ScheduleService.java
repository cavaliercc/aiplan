package com.shift.management.service;

import com.shift.management.common.PageResult;
import com.shift.management.dto.ScheduleAssignmentDTO;
import com.shift.management.dto.ScheduleDTO;

import java.time.LocalDate;

public interface ScheduleService {
    PageResult<ScheduleDTO> getSchedules(int page, int size, String storeId, LocalDate date);
    ScheduleDTO getById(Integer id);
    ScheduleDTO create(ScheduleDTO dto);
    ScheduleDTO update(Integer id, ScheduleDTO dto);
    void delete(Integer id);
    ScheduleAssignmentDTO addAssignment(Integer scheduleId, ScheduleAssignmentDTO dto);
}
