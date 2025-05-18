package com.example.order_service.order_service.DTOs.OrderDTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private Long purchaseOrderId;
    private LocalDate orderDate;
    private String status;
    private List<OrderLineItemDTO> lineItems;
}
