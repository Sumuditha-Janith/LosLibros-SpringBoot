package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.PublisherSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PublisherSaleRepository extends JpaRepository<PublisherSale, Long> {
    @Query("SELECT ps FROM PublisherSale ps WHERE ps.publisher.publisherId = :publisherId AND ps.startDate <= :currentDate AND ps.endDate >= :currentDate AND ps.isActive = true")
    List<PublisherSale> findActiveSalesByPublisher(@Param("publisherId") Long publisherId, @Param("currentDate") Date currentDate);

    @Query("SELECT ps FROM PublisherSale ps WHERE ps.startDate <= :currentDate AND ps.endDate >= :currentDate AND ps.isActive = true")
    List<PublisherSale> findAllActiveSales(@Param("currentDate") Date currentDate);

    @Query("SELECT ps FROM PublisherSale ps ORDER BY ps.startDate DESC")
    List<PublisherSale> findAllOrderedByDate();
}