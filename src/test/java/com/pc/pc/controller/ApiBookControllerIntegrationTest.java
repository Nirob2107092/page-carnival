package com.pc.pc.controller;

import com.pc.pc.model.Book;
import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;
import com.pc.pc.repository.BookRepository;
import com.pc.pc.repository.RoleRepository;
import com.pc.pc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiBookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();

        Role sellerRole = roleRepository.findByName(RoleType.SELLER).orElseThrow();

        User seller = new User();
        seller.setFullName("Test Seller");
        seller.setEmail("seller@test.com");
        seller.setPassword(passwordEncoder.encode("password"));
        seller.setEnabled(true);
        seller.setRole(sellerRole);
        seller = userRepository.save(seller);

        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setDescription("Java best practices");
        book.setPrice(new BigDecimal("45.00"));
        book.setStock(4);
        book.setCategory("Programming");
        book.setSeller(seller);
        bookRepository.save(book);
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void getAllBooksShouldReturnOkWithList() throws Exception {
        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    void getAllBooksUnauthenticatedShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void getBookByIdShouldReturnBook() throws Exception {
        Book book = bookRepository.findAll().get(0);

        mockMvc.perform(get("/api/books/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"));
    }
}
