package com.example.order_service.order_service.Respository;

import com.example.order_service.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}