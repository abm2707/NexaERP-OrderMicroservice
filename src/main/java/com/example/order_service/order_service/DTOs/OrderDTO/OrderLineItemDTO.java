package com.example.order_service.order_service.DTOs.OrderDTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderLineItemDTO {
    private Long lineItemId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
