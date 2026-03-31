package com.pc.pc.controller;

import com.pc.pc.dto.BookDto;
import com.pc.pc.exception.ApiExceptionHandler;
import com.pc.pc.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApiBookControllerIntegrationTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private ApiBookController apiBookController;

    @Test
    void getAllBooksShouldReturnOkWithList() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(apiBookController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setDescription("Java best practices");
        dto.setCategory("Programming");
        dto.setPrice(new BigDecimal("45.00"));
        dto.setStock(4);

        when(bookService.getAllBooks()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }
}
