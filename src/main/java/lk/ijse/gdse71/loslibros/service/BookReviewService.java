package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.BookReviewDTO;

import java.util.List;

public interface BookReviewService {
    BookReviewDTO addReview(BookReviewDTO reviewDTO, String username);
    BookReviewDTO updateReview(Long reviewId, BookReviewDTO reviewDTO, String username);
    void deleteReview(Long reviewId, String username);
    List<BookReviewDTO> getReviewsByBook(Long bookId);
    List<BookReviewDTO> getReviewsByUser(String username);
    BookReviewDTO getReviewById(Long reviewId);
    Double getAverageRatingByBook(Long bookId);
    Integer getReviewCountByBook(Long bookId);

    List<BookReviewDTO> getAllReviews();
}