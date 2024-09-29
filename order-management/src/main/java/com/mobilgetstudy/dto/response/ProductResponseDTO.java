package com.mobilgetstudy.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal tax;
}
