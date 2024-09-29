// File location: src/test/java/com/mobilgetstudy/service/OrderServiceTest.java

package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.event.OrderEvent;
import com.mobilgetstudy.dto.request.CreateOrderRequestDTO;
import com.mobilgetstudy.dto.OrderItemDTO;
import com.mobilgetstudy.dto.response.InventoryResponseDTO;
import com.mobilgetstudy.dto.response.OrderDTO;
import com.mobilgetstudy.exception.InsufficientStockException;
import com.mobilgetstudy.model.Order;
import com.mobilgetstudy.model.Product;
import com.mobilgetstudy.repository.OrderRepository;
import com.mobilgetstudy.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrder() {
        // Arrange
        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO();
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2);
        requestDTO.setOrderItems(Collections.singletonList(orderItemDTO));

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100));
        product.setDiscount(BigDecimal.ZERO);
        product.setTax(BigDecimal.valueOf(0.1));

        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setQuantity(10);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setTotalAmount(BigDecimal.valueOf(220));
        savedOrder.setOrderItems(new ArrayList<>()); // Initialize the orderItems list


        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryService.getInventoryByProductId(1L)).thenReturn(inventoryResponseDTO);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderDTO orderDTO = orderService.createOrder(requestDTO);

        // Assert
        assertNotNull(orderDTO);
        assertEquals(BigDecimal.valueOf(220), orderDTO.getTotalAmount());
        verify(inventoryService, times(1)).removeStock(1L, 2);
        verify(kafkaTemplate, times(1)).send(anyString(), any(OrderEvent.class));
    }

    @Test
    void testCreateOrder_InsufficientStock() {
        // Arrange
        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO();
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(20);
        requestDTO.setOrderItems(Collections.singletonList(orderItemDTO));

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryService.getInventoryByProductId(1L)).thenReturn(new InventoryResponseDTO());

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(requestDTO));
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setOrderItems(new ArrayList<>());

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        // Act
        var orders = orderService.getAllOrders();

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
        assertEquals(BigDecimal.valueOf(100), orders.get(0).getTotalAmount());
    }

    @Test
    void testGetOrderById() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setOrderItems(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        var orderDTO = orderService.getOrderById(1L);

        // Assert
        assertNotNull(orderDTO);
        assertEquals(1L, orderDTO.getId());
        assertEquals(BigDecimal.valueOf(100), orderDTO.getTotalAmount());
    }

    @Test
    void testDeleteOrder() {
        // Arrange
        doNothing().when(orderRepository).deleteById(1L);

        // Act
        orderService.deleteOrder(1L);

        // Assert
        verify(orderRepository, times(1)).deleteById(1L);
    }

}