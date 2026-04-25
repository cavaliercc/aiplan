package com.shift.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shift.management.common.PageResult;
import com.shift.management.dto.EmployeeDTO;
import com.shift.management.dto.PreferenceDTO;
import com.shift.management.entity.Employee;
import com.shift.management.entity.Position;
import com.shift.management.entity.SchedulePreference;
import com.shift.management.entity.ShiftType;
import com.shift.management.entity.Store;
import com.shift.management.entity.UnavailabilityBlock;
import com.shift.management.mapper.EmployeeMapper;
import com.shift.management.mapper.PositionMapper;
import com.shift.management.mapper.SchedulePreferenceMapper;
import com.shift.management.mapper.ShiftTypeMapper;
import com.shift.management.mapper.StoreMapper;
import com.shift.management.mapper.UnavailabilityBlockMapper;
import com.shift.management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final StoreMapper storeMapper;
    private final PositionMapper positionMapper;
    private final SchedulePreferenceMapper preferenceMapper;
    private final ShiftTypeMapper shiftTypeMapper;
    private final UnavailabilityBlockMapper unavailabilityBlockMapper;

    @Override
    public PageResult<EmployeeDTO> getEmployees(int page, int size, String name, String storeId) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) wrapper.like(Employee::getName, name);
        if (StringUtils.hasText(storeId)) wrapper.eq(Employee::getStoreId, storeId);
        Page<Employee> p = employeeMapper.selectPage(new Page<>(page, size), wrapper);
        List<EmployeeDTO> dtos = p.getRecords().stream().map(this::toDTO).collect(Collectors.toList());
        return PageResult.of(dtos, p.getTotal(), p.getCurrent(), p.getSize());
    }

    @Override
    public EmployeeDTO getById(Integer id) {
        Employee emp = employeeMapper.selectById(id);
        if (emp == null) throw new IllegalArgumentException("员工不存在: " + id);
        return toDTO(emp);
    }

    @Override
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee emp = toEntity(dto);
        employeeMapper.insert(emp);
        return toDTO(emp);
    }

    @Override
    public EmployeeDTO update(Integer id, EmployeeDTO dto) {
        if (employeeMapper.selectById(id) == null) throw new IllegalArgumentException("员工不存在: " + id);
        Employee emp = toEntity(dto);
        emp.setId(id);
        employeeMapper.updateById(emp);
        return toDTO(emp);
    }

    @Override
    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }

    @Override
    public PreferenceDTO getPreferences(Integer employeeId) {
        SchedulePreference pref = preferenceMapper.selectOne(
                new LambdaQueryWrapper<SchedulePreference>().eq(SchedulePreference::getEmployeeId, employeeId)
        );
        List<UnavailabilityBlock> blocks = unavailabilityBlockMapper.selectList(
                new LambdaQueryWrapper<UnavailabilityBlock>().eq(UnavailabilityBlock::getEmployeeId, employeeId)
        );
        return buildPreferenceDTO(employeeId, pref, blocks);
    }

    @Override
    public PreferenceDTO updatePreferences(Integer employeeId, PreferenceDTO dto) {
        SchedulePreference existing = preferenceMapper.selectOne(
                new LambdaQueryWrapper<SchedulePreference>().eq(SchedulePreference::getEmployeeId, employeeId)
        );
        SchedulePreference pref = new SchedulePreference();
        pref.setEmployeeId(employeeId);
        pref.setPreferredShiftId(dto.getPreferredShiftId());
        pref.setMaxDailyHours(dto.getMaxDailyHours());
        if (dto.getRestDays() != null) {
            pref.setRestDays(dto.getRestDays().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        if (existing == null) {
            preferenceMapper.insert(pref);
        } else {
            pref.setId(existing.getId());
            preferenceMapper.updateById(pref);
        }
        return getPreferences(employeeId);
    }

    private PreferenceDTO buildPreferenceDTO(Integer employeeId, SchedulePreference pref,
                                              List<UnavailabilityBlock> blocks) {
        PreferenceDTO dto = new PreferenceDTO();
        dto.setEmployeeId(employeeId);
        if (pref != null) {
            dto.setId(pref.getId());
            dto.setPreferredShiftId(pref.getPreferredShiftId());
            dto.setMaxDailyHours(pref.getMaxDailyHours());
            if (StringUtils.hasText(pref.getRestDays())) {
                dto.setRestDays(Arrays.stream(pref.getRestDays().split(","))
                        .map(Integer::parseInt).collect(Collectors.toList()));
            }
            if (pref.getPreferredShiftId() != null) {
                ShiftType st = shiftTypeMapper.selectById(pref.getPreferredShiftId());
                if (st != null) dto.setPreferredShiftName(st.getName());
            }
        }
        List<PreferenceDTO.UnavailabilityBlockDTO> blockDTOs = blocks.stream().map(b -> {
            PreferenceDTO.UnavailabilityBlockDTO bd = new PreferenceDTO.UnavailabilityBlockDTO();
            bd.setId(b.getId());
            bd.setDate(b.getDate() != null ? b.getDate().toString() : null);
            bd.setStartTime(b.getStartTime() != null ? b.getStartTime().toString() : null);
            bd.setEndTime(b.getEndTime() != null ? b.getEndTime().toString() : null);
            bd.setReason(b.getReason());
            return bd;
        }).collect(Collectors.toList());
        dto.setUnavailabilityBlocks(blockDTOs);
        return dto;
    }

    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setEmpNo(e.getEmpNo());
        dto.setName(e.getName());
        dto.setAvatarUrl(e.getAvatarUrl());
        dto.setPhone(e.getPhone());
        dto.setStoreId(e.getStoreId());
        dto.setPositionId(e.getPositionId());
        dto.setHireDate(e.getHireDate());
        dto.setStatus(e.getStatus());
        if (e.getStoreId() != null) {
            Store store = storeMapper.selectById(e.getStoreId());
            if (store != null) dto.setStoreName(store.getName());
        }
        if (e.getPositionId() != null) {
            Position pos = positionMapper.selectById(e.getPositionId());
            if (pos != null) dto.setPositionName(pos.getName());
        }
        return dto;
    }

    private Employee toEntity(EmployeeDTO dto) {
        Employee e = new Employee();
        e.setEmpNo(dto.getEmpNo());
        e.setName(dto.getName());
        e.setAvatarUrl(dto.getAvatarUrl());
        e.setPhone(dto.getPhone());
        e.setStoreId(dto.getStoreId());
        e.setPositionId(dto.getPositionId());
        e.setHireDate(dto.getHireDate());
        e.setStatus(dto.getStatus());
        return e;
    }
}
