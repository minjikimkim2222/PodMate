package com.podmate.domain.delivery.domain.reposiotry;

import com.podmate.domain.delivery.domain.entity.Delivery;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByPod_Id(Long podId);
    List<Delivery> findAllByDeliveryStatusAndPickupDeadlineBefore(DeliveryStatus deliveryStatus, LocalDateTime pickupDeadline);
}
