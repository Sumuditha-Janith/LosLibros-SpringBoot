package lk.ijse.gdse71.loslibros.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.gdse71.loslibros.dto.BookDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;
import lk.ijse.gdse71.loslibros.entity.*;
import lk.ijse.gdse71.loslibros.repository.*;
import lk.ijse.gdse71.loslibros.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherSaleRepository publisherSaleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository,
                           PublisherSaleRepository publisherSaleRepository,
                           ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.publisherSaleRepository = publisherSaleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);

        if (bookDTO.getBookAuthor() != null && bookDTO.getBookAuthor().getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getBookAuthor().getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found with id: " + bookDTO.getBookAuthor().getAuthorId()));
            book.setBookAuthor(author);
        }

        if (bookDTO.getBookCategory() != null && bookDTO.getBookCategory().getCategoryId() != null) {
            Category category = categoryRepository.findById(bookDTO.getBookCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + bookDTO.getBookCategory().getCategoryId()));
            book.setBookCategory(category);
        }

        if (bookDTO.getBookPublisher() != null && bookDTO.getBookPublisher().getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDTO.getBookPublisher().getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + bookDTO.getBookPublisher().getPublisherId()));
            book.setBookPublisher(publisher);
        }

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        // Get all active sales
        List<PublisherSale> activeSales = publisherSaleRepository.findAllActiveSales(new Date());

        return books.stream()
                .map(book -> {
                    BookDTO dto = convertToDTO(book);
                    // Check if book's publisher has an active sale
                    activeSales.stream()
                            .filter(sale -> sale.getPublisher().getPublisherId().equals(book.getBookPublisher().getPublisherId()))
                            .findFirst()
                            .ifPresent(sale -> {
                                double discount = sale.getDiscountPercentage();
                                dto.setDiscountedPrice(book.getBookPrice() * (1 - discount / 100));
                                dto.setSaleEndDate(sale.getEndDate());
                                dto.setSaleDescription(sale.getSaleDescription());
                            });
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        BookDTO dto = convertToDTO(book);

        // Check for active sales
        List<PublisherSale> activeSales = publisherSaleRepository
                .findActiveSalesByPublisher(book.getBookPublisher().getPublisherId(), new Date());

        if (!activeSales.isEmpty()) {
            PublisherSale activeSale = activeSales.get(0);
            double discount = activeSale.getDiscountPercentage();
            dto.setDiscountedPrice(book.getBookPrice() * (1 - discount / 100));
            dto.setSaleEndDate(activeSale.getEndDate());
            dto.setSaleDescription(activeSale.getSaleDescription());
        }

        return dto;
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
        existingBook.setBookDescription(bookDTO.getBookDescription());
        existingBook.setBookPrice(bookDTO.getBookPrice());
        existingBook.setBookQuantity(bookDTO.getBookQuantity());
        existingBook.setBookImage(bookDTO.getBookImage());

        if (bookDTO.getBookAuthor() != null && bookDTO.getBookAuthor().getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getBookAuthor().getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found with id: " + bookDTO.getBookAuthor().getAuthorId()));
            existingBook.setBookAuthor(author);
        }

        if (bookDTO.getBookCategory() != null && bookDTO.getBookCategory().getCategoryId() != null) {
            Category category = categoryRepository.findById(bookDTO.getBookCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + bookDTO.getBookCategory().getCategoryId()));
            existingBook.setBookCategory(category);
        }

        if (bookDTO.getBookPublisher() != null && bookDTO.getBookPublisher().getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDTO.getBookPublisher().getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + bookDTO.getBookPublisher().getPublisherId()));
            existingBook.setBookPublisher(publisher);
        }

        Book updatedBook = bookRepository.save(existingBook);
        return convertToDTO(updatedBook);
    }

    @Override
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByBookAuthor_AuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        return bookRepository.findByBookCategory_CategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByPublisher(Long publisherId) {
        return bookRepository.findByBookPublisher_PublisherId(publisherId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processPurchase(List<PurchaseRequest> purchaseRequests) {
        for (PurchaseRequest req : purchaseRequests) {
            Book book = bookRepository.findById(req.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + req.getBookId()));

            if (book.getBookQuantity() < req.getQuantity()) {
                throw new RuntimeException("Not enough stock for book: " + book.getBookTitle());
            }

            book.setBookQuantity(book.getBookQuantity() - req.getQuantity());
            bookRepository.save(book);
        }
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = modelMapper.map(book, BookDTO.class);

        if (book.getBookAuthor() != null && dto.getBookAuthor() != null) {
            dto.getBookAuthor().setAuthorName(book.getBookAuthor().getAuthorName());
        }
        if (book.getBookCategory() != null && dto.getBookCategory() != null) {
            dto.getBookCategory().setCategoryName(book.getBookCategory().getCategoryName());
        }
        if (book.getBookPublisher() != null && dto.getBookPublisher() != null) {
            dto.getBookPublisher().setPublisherName(book.getBookPublisher().getPublisherName());
        }

        return dto;
    }
}