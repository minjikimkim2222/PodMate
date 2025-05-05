package com.podmate.domain.delivery.domain.reposiotry;

import com.podmate.domain.delivery.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByPod_Id(Long podId);

}
