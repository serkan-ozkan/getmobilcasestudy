package com.mobilgetstudy.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderEvent {
    @JsonProperty("orderId")
    private long orderId;
    @JsonProperty("orderDate")
    private LocalDateTime orderDate;
    @JsonProperty("totalAmount ")
    private BigDecimal totalAmount;
}
