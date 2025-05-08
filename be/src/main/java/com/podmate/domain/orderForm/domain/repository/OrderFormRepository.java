package com.podmate.domain.orderForm.domain.repository;

import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.user.domain.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {
    List<OrderForm> findAllByUser(User user);
}
