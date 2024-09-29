package com.mobilgetstudy.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponseDTO {
    private Long id;
    private Long productId;
    private int quantity;

}
