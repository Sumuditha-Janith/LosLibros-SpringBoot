package lk.ijse.gdse71.loslibros.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private String bookTitle;
    private int quantity;
    private Double price;

    private Double originalPrice; //track price before discount
    private Boolean onSale; //track if item was on sale

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
