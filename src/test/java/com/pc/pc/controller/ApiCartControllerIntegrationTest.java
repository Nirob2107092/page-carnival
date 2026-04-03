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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiCartControllerIntegrationTest {

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

    private Book savedBook;

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
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setDescription("A handbook of agile software craftsmanship");
        book.setPrice(new BigDecimal("39.99"));
        book.setStock(10);
        book.setCategory("Programming");
        book.setSeller(seller);
        savedBook = bookRepository.save(book);
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void getCartShouldReturnOkWithEmptyCart() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void getCartWithWrongRoleShouldBeDenied() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(roles = "BUYER")
    void addToCartShouldReturnCartWithItem() throws Exception {
        mockMvc.perform(post("/api/cart/items")
                        .param("bookId", savedBook.getId().toString())
                        .param("quantity", "2")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }
}
