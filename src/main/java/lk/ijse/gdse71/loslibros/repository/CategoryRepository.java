package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}