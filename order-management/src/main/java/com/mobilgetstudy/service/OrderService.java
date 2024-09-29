package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.event.OrderEvent;
import com.mobilgetstudy.dto.request.CreateOrderRequestDTO;
import com.mobilgetstudy.dto.response.OrderDTO;
import com.mobilgetstudy.dto.OrderItemDTO;
import com.mobilgetstudy.exception.InsufficientStockException;
import com.mobilgetstudy.model.Order;
import com.mobilgetstudy.model.OrderItem;
import com.mobilgetstudy.model.Product;
import com.mobilgetstudy.repository.OrderItemRepository;
import com.mobilgetstudy.repository.OrderRepository;
import com.mobilgetstudy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    public OrderService(ProductRepository productRepository,
                        OrderRepository orderRepository,
                        InventoryService inventoryService,
                        KafkaTemplate<String, OrderEvent> kafkaTemplate)
    {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public OrderDTO createOrder(CreateOrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemDTO orderItemDTO : orderRequestDTO.getOrderItems()) {
            Product product = productRepository.findById(orderItemDTO.getProductId()).orElse(null);
            if(product != null) {
                if (inventoryService.getInventoryByProductId(product.getId()).getQuantity() < orderItemDTO.getQuantity()) {
                    throw new InsufficientStockException("Not enough stock for product with id: " + product.getId());
                }
                inventoryService.removeStock(product.getId(), orderItemDTO.getQuantity());
                OrderItem orderItem = OrderItem.createFromDTO(orderItemDTO, order, product);
                orderItems.add(orderItem);
            }
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(calculateTotalAmount(orderItems));

        Order createdOrder = orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(createdOrder.getId());
        orderEvent.setOrderDate(LocalDateTime.now());
        orderEvent.setTotalAmount(createdOrder.getTotalAmount());
        kafkaTemplate.send("order-topic", orderEvent);

        return convertToDTO(createdOrder);

    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> calculateItemTotalAmount(orderItem.getProduct(), orderItem.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemTotalAmount(Product product, int quantity) {
        BigDecimal price = product.getPrice();
        BigDecimal discount = product.getDiscount();
        BigDecimal tax = product.getTax();

        BigDecimal priceWithTax = price.add(price.multiply(tax)).multiply(BigDecimal.valueOf(quantity));

        return priceWithTax.subtract(priceWithTax.multiply(discount));
    }
    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderItems(order.getOrderItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return orderDTO;
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProduct().getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }

}
