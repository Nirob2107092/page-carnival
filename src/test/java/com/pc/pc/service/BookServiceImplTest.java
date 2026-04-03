package com.pc.pc.service;

import com.pc.pc.dto.BookDto;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Book;
import com.pc.pc.model.User;
import com.pc.pc.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new BookDto();
        sampleDto.setTitle("Clean Code");
        sampleDto.setAuthor("Robert C. Martin");
        sampleDto.setDescription("A handbook of agile software craftsmanship");
        sampleDto.setPrice(new BigDecimal("49.99"));
        sampleDto.setStock(10);
        sampleDto.setCategory("Programming");
    }

    @Test
    void createBookShouldSetSellerAndReturnDto() {
        User seller = new User();
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDto created = bookService.createBook(sampleDto, seller);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertSame(seller, captor.getValue().getSeller());
        assertEquals("Clean Code", created.getTitle());
    }

    @Test
    void getAllBooksShouldReturnMappedDtos() {
        Book book = new Book();
        book.setTitle("Domain-Driven Design");
        book.setAuthor("Eric Evans");
        book.setDescription("DDD book");
        book.setPrice(new BigDecimal("59.99"));
        book.setStock(7);
        book.setCategory("Architecture");

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Domain-Driven Design", result.get(0).getTitle());
    }

    @Test
    void getBookByIdShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(99L));
        assertNotNull(ex);
    }

    @Test
    void updateBookShouldUpdateFieldsAndSave() {
        User seller = new User();
        seller.setEmail("seller@test.com");

        Book existing = new Book();
        existing.setTitle("Old");
        existing.setAuthor("Old");
        existing.setDescription("Old");
        existing.setPrice(new BigDecimal("1.00"));
        existing.setStock(1);
        existing.setCategory("Old");
        existing.setSeller(seller);

        when(bookRepository.findByIdAndSeller(1L, seller)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDto updated = bookService.updateBookForSeller(1L, sampleDto, seller);

        assertEquals("Clean Code", updated.getTitle());
        assertEquals(10, updated.getStock());
        verify(bookRepository).save(existing);
    }

    @Test
    void deleteBookShouldThrowWhenBookDoesNotExist() {
        User seller = new User();
        when(bookRepository.findByIdAndSeller(42L, seller)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBookForSeller(42L, seller));
        assertNotNull(ex);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    void deleteBookShouldDeleteWhenBookExists() {
        User seller = new User();
        Book existing = new Book();
        existing.setSeller(seller);
        when(bookRepository.findByIdAndSeller(5L, seller)).thenReturn(Optional.of(existing));

        bookService.deleteBookForSeller(5L, seller);

        verify(bookRepository).delete(existing);
    }
}
