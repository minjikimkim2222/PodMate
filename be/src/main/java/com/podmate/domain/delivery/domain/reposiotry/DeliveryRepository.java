package com.podmate.domain.delivery.domain.reposiotry;

import com.podmate.domain.delivery.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
