package com.mobilgetstudy.dto.request;

import com.mobilgetstudy.dto.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDTO {

    private List<OrderItemDTO> orderItems;

}
