package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.BookReviewDTO;
import lk.ijse.gdse71.loslibros.service.BookReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
public class BookReviewController {

    private final BookReviewService reviewService;

    public BookReviewController(BookReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookReviewDTO> addReview(@RequestBody BookReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        BookReviewDTO savedReview = reviewService.addReview(reviewDTO, username);
        return ResponseEntity.ok(savedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookReviewDTO> updateReview(@PathVariable Long id, @RequestBody BookReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        BookReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO, username);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        reviewService.deleteReview(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookReviewDTO>> getReviewsByBook(@PathVariable Long bookId) {
        List<BookReviewDTO> reviews = reviewService.getReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookReviewDTO>> getReviewsByUser(Authentication authentication) {
        String username = authentication.getName();
        List<BookReviewDTO> reviews = reviewService.getReviewsByUser(username);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookReviewDTO> getReviewById(@PathVariable Long id) {
        BookReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/book/{bookId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long bookId) {
        Double averageRating = reviewService.getAverageRatingByBook(bookId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<Integer> getReviewCount(@PathVariable Long bookId) {
        Integer count = reviewService.getReviewCountByBook(bookId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<BookReviewDTO>> getAllReviews() {
        List<BookReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

}