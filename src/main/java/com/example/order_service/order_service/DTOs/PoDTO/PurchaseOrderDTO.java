package com.example.order_service.order_service.DTOs.PoDTO;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderDTO {
    private Long poId;
    private List<PurchaseOrderLineItemDTO> lineItems;
}
