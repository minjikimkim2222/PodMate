package com.podmate.domain.orderForm.domain.repository;

import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.orderForm.domain.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderForm(OrderForm orderForm);
}
