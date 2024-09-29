package com.mobilgetstudy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private Long id;
    private Long productId;
    private int quantity;

}
