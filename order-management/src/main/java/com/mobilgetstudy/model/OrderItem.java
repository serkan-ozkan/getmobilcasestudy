package com.mobilgetstudy.model;

import com.mobilgetstudy.dto.OrderItemDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public static OrderItem createFromDTO(OrderItemDTO orderItemDTO, Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setOrder(order);
        return orderItem;
    }

}
