package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.BookReviewDTO;
import lk.ijse.gdse71.loslibros.entity.Book;
import lk.ijse.gdse71.loslibros.entity.BookReview;
import lk.ijse.gdse71.loslibros.entity.User;
import lk.ijse.gdse71.loslibros.repository.BookRepository;
import lk.ijse.gdse71.loslibros.repository.BookReviewRepository;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lk.ijse.gdse71.loslibros.service.BookReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookReviewServiceImpl implements BookReviewService {

    private final BookReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BookReviewServiceImpl(BookReviewRepository reviewRepository,
                                 BookRepository bookRepository,
                                 UserRepository userRepository,
                                 ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookReviewDTO addReview(BookReviewDTO reviewDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Book book = bookRepository.findById(reviewDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + reviewDTO.getBookId()));

        // Check if user already reviewed this book
        reviewRepository.findByBook_BookIdAndUser_Id(book.getBookId(), user.getId())
                .ifPresent(review -> {
                    throw new RuntimeException("You have already reviewed this book");
                });

        // Validate rating
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        BookReview review = modelMapper.map(reviewDTO, BookReview.class);
        review.setBook(book);
        review.setUser(user);

        BookReview savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Override
    public BookReviewDTO updateReview(Long reviewId, BookReviewDTO reviewDTO, String username) {
        BookReview existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check if the user owns this review or is admin/employee
        if (!existingReview.getUser().getId().equals(user.getId()) &&
                !user.getRole().name().equals("ADMIN") &&
                !user.getRole().name().equals("EMPLOYEE")) {
            throw new RuntimeException("You can only update your own reviews");
        }

        // Validate rating
        if (reviewDTO.getRating() != null && (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5)) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        if (reviewDTO.getRating() != null) {
            existingReview.setRating(reviewDTO.getRating());
        }

        if (reviewDTO.getComment() != null) {
            existingReview.setComment(reviewDTO.getComment());
        }

        BookReview updatedReview = reviewRepository.save(existingReview);
        return convertToDTO(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId, String username) {
        BookReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check if the user owns this review or is admin/employee
        if (!review.getUser().getId().equals(user.getId()) &&
                !user.getRole().name().equals("ADMIN") &&
                !user.getRole().name().equals("EMPLOYEE")) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<BookReviewDTO> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBook_BookId(bookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReviewDTO> getReviewsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return reviewRepository.findByUser_Id(user.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookReviewDTO getReviewById(Long reviewId) {
        BookReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        return convertToDTO(review);
    }

    @Override
    public Double getAverageRatingByBook(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

    @Override
    public Integer getReviewCountByBook(Long bookId) {
        return reviewRepository.countByBookId(bookId);
    }

    private BookReviewDTO convertToDTO(BookReview review) {
        BookReviewDTO dto = modelMapper.map(review, BookReviewDTO.class);
        dto.setBookId(review.getBook().getBookId());
        dto.setBookTitle(review.getBook().getBookTitle());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        return dto;
    }

    @Override
    public List<BookReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}