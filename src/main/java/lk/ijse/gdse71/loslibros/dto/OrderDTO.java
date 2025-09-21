package lk.ijse.gdse71.loslibros.dto;

import lk.ijse.gdse71.loslibros.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String username;
    private String userEmail;
    private String userAddress;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> items;
}