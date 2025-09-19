package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}