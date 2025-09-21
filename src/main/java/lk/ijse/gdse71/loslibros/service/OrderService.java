package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.OrderDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;
import lk.ijse.gdse71.loslibros.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String username, List<PurchaseRequest> requests);
    List<OrderDTO> getOrdersByUser(String username);
    List<OrderDTO> getAllOrders();
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
}