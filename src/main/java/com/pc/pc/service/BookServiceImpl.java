package com.pc.pc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pc.pc.dto.BookDto;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Book;
import com.pc.pc.model.User;
import com.pc.pc.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setStock(book.getStock());
        dto.setCategory(book.getCategory());
        if (book.getSeller() != null) {
            dto.setSellerName(book.getSeller().getFullName());
        }
        return dto;
    }

    private Book mapToEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        book.setCategory(dto.getCategory());
        return book;
    }

    @Override
    public BookDto createBook(BookDto bookDto, User seller) {
        Book book = mapToEntity(bookDto);
        book.setSeller(seller);
        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBuyerVisibleBooks() {
        return bookRepository.findBySeller_EnabledTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksBySeller(User seller) {
        return bookRepository.findBySeller(seller)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        return mapToDto(bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id)));
    }

    @Override
    public BookDto getBookByIdForSeller(Long id, User seller) {
        return mapToDto(getOwnedBook(id, seller));
    }

    @Override
    public BookDto updateBook(Long id, BookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        book.setCategory(dto.getCategory());

        return mapToDto(bookRepository.save(book));
    }

    @Override
    public BookDto updateBookForSeller(Long id, BookDto dto, User seller) {
        Book book = getOwnedBook(id, seller);

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        book.setCategory(dto.getCategory());

        return mapToDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteBookForSeller(Long id, User seller) {
        Book book = getOwnedBook(id, seller);
        bookRepository.delete(book);
    }

    private Book getOwnedBook(Long id, User seller) {
        return bookRepository.findByIdAndSeller(id, seller)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }
}