package com.example.order_service.order_service.Controller;

import com.example.order_service.order_service.DTOs.OrderDTO.OrderDTO;
import com.example.order_service.order_service.Entity.Order;
import com.example.order_service.order_service.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.createOrderFromDTO(orderDTO);
        return ResponseEntity.ok(orderService.mapToDTO(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return orderService.getOrder(id)
                .map(order -> ResponseEntity.ok(orderService.mapToDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, body.get("status")));
    }
}
