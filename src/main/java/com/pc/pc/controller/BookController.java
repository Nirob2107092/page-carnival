package com.pc.pc.controller;

import com.pc.pc.dto.BookDto;
import com.pc.pc.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDto());
        return "create-book";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto) {
        bookService.createBook(bookDto);
        return "redirect:/seller/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "edit-book";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookDto bookDto) {
        bookService.updateBook(id, bookDto);
        return "redirect:/seller/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/seller/books";
    }
}