package com.mobilgetstudy.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequestDTO {
    private String name;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal tax;
    private Integer initialStock;
}
