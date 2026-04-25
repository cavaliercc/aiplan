package com.shift.management.service;

import com.shift.management.dto.ShiftSwapRequestDTO;

public interface ShiftSwapService {
    ShiftSwapRequestDTO createRequest(ShiftSwapRequestDTO dto);
    ShiftSwapRequestDTO approveRequest(Integer id, String reviewerUsername);
    ShiftSwapRequestDTO rejectRequest(Integer id, String reviewerUsername);
}
