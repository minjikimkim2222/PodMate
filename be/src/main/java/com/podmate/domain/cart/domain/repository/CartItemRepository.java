package com.podmate.domain.cart.domain.repository;

import com.podmate.domain.cart.domain.entity.CartItem;
import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByPlatformInfo(PlatformInfo platformInfo);
}
