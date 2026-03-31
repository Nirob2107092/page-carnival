package com.pc.pc.controller;

import com.pc.pc.dto.CartDto;
import com.pc.pc.exception.ApiExceptionHandler;
import com.pc.pc.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApiCartControllerIntegrationTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private ApiCartController apiCartController;

    @Test
    void getCartShouldReturnOk() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(apiCartController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        CartDto cart = new CartDto();
        when(cartService.getCart()).thenReturn(cart);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }
}
