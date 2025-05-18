package com.example.order_service.order_service.Services;

import com.example.order_service.order_service.Client.PurchaseOrderClient;
import com.example.order_service.order_service.DTOs.OrderDTO.OrderDTO;
import com.example.order_service.order_service.DTOs.OrderDTO.OrderLineItemDTO;
import com.example.order_service.order_service.DTOs.PoDTO.PurchaseOrderDTO;
import com.example.order_service.order_service.DTOs.PoDTO.PurchaseOrderLineItemDTO;
import com.example.order_service.order_service.Entity.Order;
import com.example.order_service.order_service.Entity.OrderLineItem;
import com.example.order_service.order_service.Respository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseOrderClient purchaseOrderClient;

    @Transactional
    public Order createOrderFromDTO(OrderDTO orderDTO) {

        // 1. Fetch and validate PO
        PurchaseOrderDTO po = purchaseOrderClient.getPurchaseOrder(orderDTO.getPurchaseOrderId());
        if (po == null) {
            throw new EntityNotFoundException("Purchase Order not found");
        }

        // 2. Validate line items
        for (OrderLineItemDTO orderItem : orderDTO.getLineItems()) {
            PurchaseOrderLineItemDTO poItem = po.getLineItems().stream()
                    .filter(item -> item.getProductId().equals(orderItem.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (poItem == null) {
                throw new IllegalArgumentException("Product " + orderItem.getProductId() + " not in Purchase Order");
            }
            if (orderItem.getQuantity() > poItem.getQuantity()) {
                throw new IllegalArgumentException("Ordered quantity for product " + orderItem.getProductId() +
                        " exceeds available quantity in Purchase Order");
            }
        }

        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setPurchaseOrderId(orderDTO.getPurchaseOrderId());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(orderDTO.getStatus());

        List<OrderLineItem> lineItems = orderDTO.getLineItems().stream().map(itemDTO -> {
            OrderLineItem item = new OrderLineItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setOrder(order); // set the back-reference
            return item;
        }).toList();

        order.setLineItems(lineItems);
        return orderRepository.save(order);
    }

    public OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setPurchaseOrderId(order.getPurchaseOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());

        List<OrderLineItemDTO> lineItemDTOs = order.getLineItems().stream().map(lineItem -> {
            OrderLineItemDTO itemDTO = new OrderLineItemDTO();
            itemDTO.setLineItemId(lineItem.getLineItemId());
            itemDTO.setProductId(lineItem.getProductId());
            itemDTO.setQuantity(lineItem.getQuantity());
            itemDTO.setPrice(lineItem.getPrice());
            return itemDTO;
        }).toList();

        dto.setLineItems(lineItemDTOs);
        return dto;
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
