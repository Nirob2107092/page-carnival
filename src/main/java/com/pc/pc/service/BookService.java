package com.pc.pc.service;

import java.util.List;

import com.pc.pc.dto.BookDto;
import com.pc.pc.model.User;

public interface BookService {
    BookDto createBook(BookDto bookDto, User seller);
    BookDto updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
    BookDto getBookById(Long id);
    List<BookDto> getAllBooks();
}