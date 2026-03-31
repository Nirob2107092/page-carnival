package com.pc.pc.controller;

import com.pc.pc.dto.UserRegistrationDto;
import com.pc.pc.model.RoleType;
import com.pc.pc.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AuthControllerIntegrationTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerUserShouldRedirectToLogin() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        mockMvc.perform(post("/register")
                        .param("fullName", "Alice")
                        .param("email", "alice@example.com")
                        .param("password", "pass123")
                        .param("role", RoleType.BUYER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login?registered"));
    }
}
