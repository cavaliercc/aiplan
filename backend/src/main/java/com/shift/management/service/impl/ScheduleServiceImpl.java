package com.shift.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shift.management.common.PageResult;
import com.shift.management.dto.ScheduleAssignmentDTO;
import com.shift.management.dto.ScheduleDTO;
import com.shift.management.entity.Employee;
import com.shift.management.entity.Schedule;
import com.shift.management.entity.ScheduleAssignment;
import com.shift.management.entity.ShiftType;
import com.shift.management.entity.Store;
import com.shift.management.mapper.EmployeeMapper;
import com.shift.management.mapper.ScheduleAssignmentMapper;
import com.shift.management.mapper.ScheduleMapper;
import com.shift.management.mapper.ShiftTypeMapper;
import com.shift.management.mapper.StoreMapper;
import com.shift.management.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleAssignmentMapper assignmentMapper;
    private final StoreMapper storeMapper;
    private final ShiftTypeMapper shiftTypeMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public PageResult<ScheduleDTO> getSchedules(int page, int size, String storeId, LocalDate date) {
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(storeId)) wrapper.eq(Schedule::getStoreId, storeId);
        if (date != null) wrapper.eq(Schedule::getShiftDate, date);
        Page<Schedule> p = scheduleMapper.selectPage(new Page<>(page, size), wrapper);
        List<ScheduleDTO> dtos = p.getRecords().stream().map(this::toDTO).collect(Collectors.toList());
        return PageResult.of(dtos, p.getTotal(), p.getCurrent(), p.getSize());
    }

    @Override
    public ScheduleDTO getById(Integer id) {
        Schedule s = scheduleMapper.selectById(id);
        if (s == null) throw new IllegalArgumentException("排班不存在: " + id);
        return toDTO(s);
    }

    @Override
    public ScheduleDTO create(ScheduleDTO dto) {
        Schedule s = toEntity(dto);
        scheduleMapper.insert(s);
        return toDTO(s);
    }

    @Override
    public ScheduleDTO update(Integer id, ScheduleDTO dto) {
        if (scheduleMapper.selectById(id) == null) throw new IllegalArgumentException("排班不存在: " + id);
        Schedule s = toEntity(dto);
        s.setId(id);
        scheduleMapper.updateById(s);
        return toDTO(s);
    }

    @Override
    public void delete(Integer id) {
        scheduleMapper.deleteById(id);
    }

    @Override
    public ScheduleAssignmentDTO addAssignment(Integer scheduleId, ScheduleAssignmentDTO dto) {
        ScheduleAssignment a = new ScheduleAssignment();
        a.setScheduleId(scheduleId);
        a.setEmployeeId(dto.getEmployeeId());
        a.setStatus(dto.getStatus());
        assignmentMapper.insert(a);
        return toAssignmentDTO(a);
    }

    private ScheduleDTO toDTO(Schedule s) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(s.getId());
        dto.setStoreId(s.getStoreId());
        dto.setShiftDate(s.getShiftDate());
        dto.setShiftTypeId(s.getShiftTypeId());
        dto.setZone(s.getZone());
        dto.setVehicleId(s.getVehicleId());
        dto.setStatus(s.getStatus());
        dto.setCreatedBy(s.getCreatedBy());
        if (s.getStoreId() != null) {
            Store store = storeMapper.selectById(s.getStoreId());
            if (store != null) dto.setStoreName(store.getName());
        }
        if (s.getShiftTypeId() != null) {
            ShiftType st = shiftTypeMapper.selectById(s.getShiftTypeId());
            if (st != null) dto.setShiftTypeName(st.getName());
        }
        List<ScheduleAssignment> assignments = assignmentMapper.selectList(
                new LambdaQueryWrapper<ScheduleAssignment>().eq(ScheduleAssignment::getScheduleId, s.getId())
        );
        dto.setAssignments(assignments.stream().map(this::toAssignmentDTO).collect(Collectors.toList()));
        return dto;
    }

    private ScheduleAssignmentDTO toAssignmentDTO(ScheduleAssignment a) {
        ScheduleAssignmentDTO dto = new ScheduleAssignmentDTO();
        dto.setId(a.getId());
        dto.setScheduleId(a.getScheduleId());
        dto.setEmployeeId(a.getEmployeeId());
        dto.setCheckedIn(a.getCheckedIn());
        dto.setCheckedOut(a.getCheckedOut());
        dto.setStatus(a.getStatus());
        if (a.getEmployeeId() != null) {
            Employee emp = employeeMapper.selectById(a.getEmployeeId());
            if (emp != null) dto.setEmployeeName(emp.getName());
        }
        return dto;
    }

    private Schedule toEntity(ScheduleDTO dto) {
        Schedule s = new Schedule();
        s.setStoreId(dto.getStoreId());
        s.setShiftDate(dto.getShiftDate());
        s.setShiftTypeId(dto.getShiftTypeId());
        s.setZone(dto.getZone());
        s.setVehicleId(dto.getVehicleId());
        s.setStatus(dto.getStatus());
        s.setCreatedBy(dto.getCreatedBy());
        return s;
    }
}
