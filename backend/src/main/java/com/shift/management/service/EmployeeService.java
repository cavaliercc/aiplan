package com.shift.management.service;

import com.shift.management.common.PageResult;
import com.shift.management.dto.EmployeeDTO;
import com.shift.management.dto.PreferenceDTO;

public interface EmployeeService {
    PageResult<EmployeeDTO> getEmployees(int page, int size, String name, String storeId);
    EmployeeDTO getById(Integer id);
    EmployeeDTO create(EmployeeDTO dto);
    EmployeeDTO update(Integer id, EmployeeDTO dto);
    void delete(Integer id);
    PreferenceDTO getPreferences(Integer employeeId);
    PreferenceDTO updatePreferences(Integer employeeId, PreferenceDTO dto);
}
