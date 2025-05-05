package com.podmate.domain.cart.domain.repository;

import com.podmate.domain.cart.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
