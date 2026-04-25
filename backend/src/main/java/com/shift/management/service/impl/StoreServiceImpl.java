package com.shift.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shift.management.common.PageResult;
import com.shift.management.dto.StoreDTO;
import com.shift.management.entity.Employee;
import com.shift.management.entity.Store;
import com.shift.management.mapper.EmployeeMapper;
import com.shift.management.mapper.StoreMapper;
import com.shift.management.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public PageResult<StoreDTO> getStores(int page, int size, String name, String status) {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) wrapper.like(Store::getName, name);
        if (StringUtils.hasText(status)) wrapper.eq(Store::getStatus, status);
        Page<Store> p = storeMapper.selectPage(new Page<>(page, size), wrapper);
        List<StoreDTO> dtos = p.getRecords().stream().map(this::toDTO).collect(Collectors.toList());
        return PageResult.of(dtos, p.getTotal(), p.getCurrent(), p.getSize());
    }

    @Override
    public StoreDTO getById(String id) {
        Store store = storeMapper.selectById(id);
        if (store == null) throw new IllegalArgumentException("门店不存在: " + id);
        return toDTO(store);
    }

    @Override
    public StoreDTO create(StoreDTO dto) {
        Store store = toEntity(dto);
        storeMapper.insert(store);
        return toDTO(store);
    }

    @Override
    public StoreDTO update(String id, StoreDTO dto) {
        if (storeMapper.selectById(id) == null) throw new IllegalArgumentException("门店不存在: " + id);
        Store store = toEntity(dto);
        store.setId(id);
        storeMapper.updateById(store);
        return toDTO(store);
    }

    @Override
    public void delete(String id) {
        storeMapper.deleteById(id);
    }

    private StoreDTO toDTO(Store s) {
        StoreDTO dto = new StoreDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setCity(s.getCity());
        dto.setProvince(s.getProvince());
        dto.setAddress(s.getAddress());
        dto.setPhone(s.getPhone());
        dto.setOpenTime(s.getOpenTime());
        dto.setCloseTime(s.getCloseTime());
        dto.setStatus(s.getStatus());
        dto.setManagerId(s.getManagerId());
        dto.setResumeDate(s.getResumeDate());
        if (s.getManagerId() != null) {
            Employee mgr = employeeMapper.selectById(s.getManagerId());
            if (mgr != null) dto.setManagerName(mgr.getName());
        }
        long empCount = employeeMapper.selectCount(
                new LambdaQueryWrapper<Employee>().eq(Employee::getStoreId, s.getId())
        );
        dto.setEmployeeCount((int) empCount);
        return dto;
    }

    private Store toEntity(StoreDTO dto) {
        Store s = new Store();
        s.setId(dto.getId());
        s.setName(dto.getName());
        s.setCity(dto.getCity());
        s.setProvince(dto.getProvince());
        s.setAddress(dto.getAddress());
        s.setPhone(dto.getPhone());
        s.setOpenTime(dto.getOpenTime());
        s.setCloseTime(dto.getCloseTime());
        s.setStatus(dto.getStatus());
        s.setManagerId(dto.getManagerId());
        s.setResumeDate(dto.getResumeDate());
        return s;
    }
}
