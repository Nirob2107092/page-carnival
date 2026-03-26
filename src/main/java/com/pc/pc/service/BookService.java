package com.pc.pc.service;

import java.util.List;

import com.pc.pc.dto.BookDto;

public interface BookService {
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
    BookDto getBookById(Long id);
    List<BookDto> getAllBooks();
}