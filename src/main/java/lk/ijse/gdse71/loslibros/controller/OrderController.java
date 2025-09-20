package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.OrderDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;
import lk.ijse.gdse71.loslibros.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @PreAuthorize("hasAnyRole('USER','EMPLOYEE','ADMIN')")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody List<PurchaseRequest> items,
                                               Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(orderService.placeOrder(username, items));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','EMPLOYEE','ADMIN')")
    public ResponseEntity<List<OrderDTO>> myOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getOrdersByUser(auth.getName()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> allOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
