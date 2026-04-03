package com.pc.pc.controller;

import com.pc.pc.dto.BookDto;
import com.pc.pc.security.CustomUserDetails;
import com.pc.pc.service.BookService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('SELLER')")
@RequestMapping("/seller/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("books", bookService.getBooksBySeller(userDetails.getUser()));
        return "books";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDto());
        return "create-book";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        bookService.createBook(bookDto, userDetails.getUser());
        return "redirect:/seller/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        model.addAttribute("book", bookService.getBookByIdForSeller(id, userDetails.getUser()));
        return "edit-book";
    }

    @PutMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookDto bookDto,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        bookService.updateBookForSeller(id, bookDto, userDetails.getUser());
        return "redirect:/seller/books";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        bookService.deleteBookForSeller(id, userDetails.getUser());
        return "redirect:/seller/books";
    }
}