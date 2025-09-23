package lk.ijse.gdse71.loslibros.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.gdse71.loslibros.dto.OrderDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;
import lk.ijse.gdse71.loslibros.entity.*;
import lk.ijse.gdse71.loslibros.repository.BookRepository;
import lk.ijse.gdse71.loslibros.repository.OrderRepository;
import lk.ijse.gdse71.loslibros.repository.PublisherSaleRepository;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lk.ijse.gdse71.loslibros.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PublisherSaleRepository publisherSaleRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            BookRepository bookRepository,
                            UserRepository userRepository,
                            PublisherSaleRepository publisherSaleRepository,
                            ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.publisherSaleRepository = publisherSaleRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(String username, List<PurchaseRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException("No items provided for order.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<OrderItem> items = requests.stream().map(req -> {
            if (req == null || req.getBookId() == null || req.getQuantity() <= 0) {
                throw new RuntimeException("Invalid purchase request: " + req);
            }

            Book book = bookRepository.findById(req.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + req.getBookId()));

            if (book.getBookQuantity() < req.getQuantity()) {
                throw new RuntimeException("Not enough stock for book: " + book.getBookTitle());
            }

            Double finalPrice = book.getBookPrice();
            Boolean onSale = false;

            List<PublisherSale> activeSales = publisherSaleRepository
                    .findActiveSalesByPublisher(book.getBookPublisher().getPublisherId(), new Date());

            if (!activeSales.isEmpty()) {
                PublisherSale activeSale = activeSales.get(0);
                double discount = activeSale.getDiscountPercentage();
                finalPrice = book.getBookPrice() * (1 - discount / 100);
                onSale = true;
            }

            book.setBookQuantity(book.getBookQuantity() - req.getQuantity());
            bookRepository.save(book);

            return OrderItem.builder()
                    .bookId(book.getBookId())
                    .bookTitle(book.getBookTitle())
                    .quantity(req.getQuantity())
                    .price(finalPrice)
                    .originalPrice(book.getBookPrice()) // Store original price
                    .onSale(onSale) // Mark if purchased during sale
                    .build();
        }).collect(Collectors.toList());

        double total = items.stream()
                .mapToDouble(it -> (it.getPrice() == null ? 0.0 : it.getPrice()) * it.getQuantity())
                .sum();

        Order order = Order.builder()
                .username(username)
                .userEmail(user.getEmail())
                .userAddress(user.getAddress())
                .totalAmount(total)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .items(items)
                .build();

        items.forEach(i -> i.setOrder(order));

        Order saved = orderRepository.save(order);
        return mapper.map(saved, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String username) {
        return orderRepository.findByUsername(username).stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllOrderByDateDesc().stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
}