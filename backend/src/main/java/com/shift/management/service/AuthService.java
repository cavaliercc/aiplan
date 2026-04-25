package com.shift.management.service;

import com.shift.management.dto.LoginRequest;
import com.shift.management.dto.LoginResponse;
import com.shift.management.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void register(RegisterRequest request);
}
