public interface BookService {
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
    BookDto getBookById(Long id);
    List<BookDto> getAllBooks();
}