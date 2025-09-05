package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.BookDTO;
import lk.ijse.gdse71.loslibros.entity.Book;
import lk.ijse.gdse71.loslibros.repository.BookRepository;
import lk.ijse.gdse71.loslibros.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existingBook.setBookTitle(bookDTO.getBookTitle());
        existingBook.setBookPrice(bookDTO.getBookPrice());
        existingBook.setBookQuantity(bookDTO.getBookQuantity());
        existingBook.setBookImage(bookDTO.getBookImage());

        Book updatedBook = bookRepository.save(existingBook);
        return modelMapper.map(updatedBook, BookDTO.class);
    }

    @Override
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByBookAuthor_AuthorId(authorId).stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        return bookRepository.findByBookCategory_CategoryId(categoryId).stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }
}