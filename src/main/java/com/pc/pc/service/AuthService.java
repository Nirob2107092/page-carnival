package com.pc.pc.service;

import com.pc.pc.dto.UserRegistrationDto;

public interface AuthService {
    void registerUser(UserRegistrationDto registrationDto);
}