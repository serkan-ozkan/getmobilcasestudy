package com.mobilgetstudy.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequestDTO {
        private Long productId;
        private Integer stockQuantity;
}
