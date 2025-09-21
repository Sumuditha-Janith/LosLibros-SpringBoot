package lk.ijse.gdse71.loslibros.repository;

import lk.ijse.gdse71.loslibros.entity.Order;
import lk.ijse.gdse71.loslibros.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsername(String username);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status")
    List<Order> findByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllOrderByDateDesc();
}