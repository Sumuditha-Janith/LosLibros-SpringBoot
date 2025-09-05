package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO saveBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    void deleteBook(Long id);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    List<BookDTO> getBooksByAuthor(Long authorId);
    List<BookDTO> getBooksByCategory(Long categoryId);
}