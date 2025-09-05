package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookAuthor_AuthorId(Long authorId);
    List<Book> findByBookCategory_CategoryId(Long categoryId);
}