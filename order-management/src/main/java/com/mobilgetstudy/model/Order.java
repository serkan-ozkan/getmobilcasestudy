package com.mobilgetstudy.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

    public void calculateTotalAmount() {
        totalAmount = orderItems.stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Add tax
        totalAmount = totalAmount.add(totalAmount.multiply(BigDecimal.valueOf(0.18)));

        // Apply discount
        totalAmount = totalAmount.subtract(totalAmount.multiply(BigDecimal.valueOf(0.1)));
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        orderItems.forEach(orderItem -> orderItem.setOrder(this));
    }
}
