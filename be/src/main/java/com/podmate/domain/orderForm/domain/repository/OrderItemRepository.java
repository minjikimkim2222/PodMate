package com.podmate.domain.orderForm.domain.repository;

import com.podmate.domain.orderForm.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
