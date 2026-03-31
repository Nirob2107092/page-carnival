package com.pc.pc.service;

import com.pc.pc.dto.UserRegistrationDto;
import com.pc.pc.exception.DuplicateEmailException;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;
import com.pc.pc.repository.RoleRepository;
import com.pc.pc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegistrationDto request;

    @BeforeEach
    void setUp() {
        request = new UserRegistrationDto();
        request.setFullName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("password123");
        request.setRole(RoleType.BUYER);
    }

    @Test
    void registerUserShouldThrowWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.registerUser(request));
    }

    @Test
    void registerUserShouldThrowWhenRoleIsMissing() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName(RoleType.BUYER)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.registerUser(request));
    }

    @Test
    void registerUserShouldEncodePasswordAndSaveUser() {
        Role buyerRole = new Role(RoleType.BUYER);

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName(RoleType.BUYER)).thenReturn(Optional.of(buyerRole));
        when(passwordEncoder.encode("password123")).thenReturn("encoded-pass");

        authService.registerUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("encoded-pass", userCaptor.getValue().getPassword());
        assertEquals(RoleType.BUYER, userCaptor.getValue().getRole().getName());
    }
}
