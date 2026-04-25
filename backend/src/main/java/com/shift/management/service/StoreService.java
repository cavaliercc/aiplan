package com.shift.management.service;

import com.shift.management.common.PageResult;
import com.shift.management.dto.StoreDTO;

public interface StoreService {
    PageResult<StoreDTO> getStores(int page, int size, String name, String status);
    StoreDTO getById(String id);
    StoreDTO create(StoreDTO dto);
    StoreDTO update(String id, StoreDTO dto);
    void delete(String id);
}
