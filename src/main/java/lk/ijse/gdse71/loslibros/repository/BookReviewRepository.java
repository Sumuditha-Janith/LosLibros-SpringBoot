package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByBook_BookId(Long bookId);
    List<BookReview> findByUser_Id(Long userId);
    Optional<BookReview> findByBook_BookIdAndUser_Id(Long bookId, Long userId);

    @Query("SELECT AVG(r.rating) FROM BookReview r WHERE r.book.bookId = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM BookReview r WHERE r.book.bookId = :bookId")
    Integer countByBookId(@Param("bookId") Long bookId);
}