package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}