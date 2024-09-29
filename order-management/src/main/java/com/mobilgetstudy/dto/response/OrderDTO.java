package com.mobilgetstudy.dto.response;

import com.mobilgetstudy.dto.OrderItemDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;
}
