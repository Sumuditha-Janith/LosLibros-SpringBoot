package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.OrderDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String username, List<PurchaseRequest> items);
    List<OrderDTO> getOrdersByUser(String username);
    List<OrderDTO> getAllOrders(); // admin
}
