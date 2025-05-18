package com.example.order_service.order_service.DTOs.PoDTO;

import lombok.Data;

@Data
public class PurchaseOrderLineItemDTO {
    private Long lineItemId;
    private Long productId;
    private Integer quantity;
}