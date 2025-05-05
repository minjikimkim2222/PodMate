package com.podmate.domain.orderForm.domain.repository;

import com.podmate.domain.orderForm.domain.entity.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {

}
