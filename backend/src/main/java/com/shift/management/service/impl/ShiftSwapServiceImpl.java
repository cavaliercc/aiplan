package com.shift.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shift.management.dto.ShiftSwapRequestDTO;
import com.shift.management.entity.Employee;
import com.shift.management.entity.ShiftSwapRequest;
import com.shift.management.entity.User;
import com.shift.management.mapper.EmployeeMapper;
import com.shift.management.mapper.ShiftSwapRequestMapper;
import com.shift.management.mapper.UserMapper;
import com.shift.management.service.ShiftSwapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShiftSwapServiceImpl implements ShiftSwapService {

    private final ShiftSwapRequestMapper swapMapper;
    private final EmployeeMapper employeeMapper;
    private final UserMapper userMapper;

    @Override
    public ShiftSwapRequestDTO createRequest(ShiftSwapRequestDTO dto) {
        ShiftSwapRequest req = new ShiftSwapRequest();
        req.setRequesterId(dto.getRequesterId());
        req.setTargetId(dto.getTargetId());
        req.setRequesterSchedId(dto.getRequesterSchedId());
        req.setTargetSchedId(dto.getTargetSchedId());
        req.setReason(dto.getReason());
        req.setStatus("PENDING");
        swapMapper.insert(req);
        return toDTO(req);
    }

    @Override
    public ShiftSwapRequestDTO approveRequest(Integer id, String reviewerUsername) {
        return updateStatus(id, "APPROVED", reviewerUsername);
    }

    @Override
    public ShiftSwapRequestDTO rejectRequest(Integer id, String reviewerUsername) {
        return updateStatus(id, "REJECTED", reviewerUsername);
    }

    private ShiftSwapRequestDTO updateStatus(Integer id, String status, String reviewerUsername) {
        ShiftSwapRequest req = swapMapper.selectById(id);
        if (req == null) throw new IllegalArgumentException("换班申请不存在: " + id);
        User reviewer = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, reviewerUsername)
        );
        req.setStatus(status);
        req.setReviewedAt(LocalDateTime.now());
        if (reviewer != null) req.setReviewedBy(reviewer.getId());
        swapMapper.updateById(req);
        return toDTO(req);
    }

    private ShiftSwapRequestDTO toDTO(ShiftSwapRequest req) {
        ShiftSwapRequestDTO dto = new ShiftSwapRequestDTO();
        dto.setId(req.getId());
        dto.setRequesterId(req.getRequesterId());
        dto.setTargetId(req.getTargetId());
        dto.setRequesterSchedId(req.getRequesterSchedId());
        dto.setTargetSchedId(req.getTargetSchedId());
        dto.setReason(req.getReason());
        dto.setStatus(req.getStatus());
        dto.setReviewedBy(req.getReviewedBy());
        dto.setReviewedAt(req.getReviewedAt());
        dto.setCreatedAt(req.getCreatedAt());
        if (req.getRequesterId() != null) {
            Employee e = employeeMapper.selectById(req.getRequesterId());
            if (e != null) dto.setRequesterName(e.getName());
        }
        if (req.getTargetId() != null) {
            Employee e = employeeMapper.selectById(req.getTargetId());
            if (e != null) dto.setTargetName(e.getName());
        }
        return dto;
    }
}
