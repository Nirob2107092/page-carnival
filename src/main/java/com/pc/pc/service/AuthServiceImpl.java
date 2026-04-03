package com.pc.pc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pc.pc.dto.UserRegistrationDto;
import com.pc.pc.exception.DuplicateEmailException;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;
import com.pc.pc.repository.RoleRepository;
import com.pc.pc.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegistrationDto registrationDto) {
        if (registrationDto.getRole() == RoleType.ADMIN) {
            throw new IllegalArgumentException("Admin registration is not allowed");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new DuplicateEmailException(registrationDto.getEmail());
        }

        Role role = roleRepository.findByName(registrationDto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role", null));

        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);
        user.setRole(role);

        userRepository.save(user);
    }
}