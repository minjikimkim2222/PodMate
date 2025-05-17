package com.podmate.domain.delivery.domain.entity;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.user.domain.enums.Role;
import com.podmate.domain.user.domain.enums.SocialType;
import com.podmate.domain.user.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pod_id")
    private Pod pod;

    @Column(nullable = false)
    private String trackingNum;  //운송장 번호

    @Column(nullable = false)
    private String courierCompany;  //택배사

    @Column(nullable = true)
    private LocalDateTime pickupDeadline;   //픽업 기한     deliveryStatus가 DELIVERED가 되면 현재로부터 5일 후로 업데이트

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;  //배송 상태

    @Builder
    public Delivery(Pod pod, String trackingNum, String courierCompany, LocalDateTime pickupDeadline, DeliveryStatus deliveryStatus) {
        this.pod = pod;
        this.trackingNum = trackingNum;
        this.courierCompany = courierCompany;
        this.pickupDeadline = pickupDeadline;
        this.deliveryStatus = deliveryStatus;
    }

    public void updateDeliveryStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
        if (status == DeliveryStatus.DELIVERED) {
            this.pickupDeadline = LocalDateTime.now().plusSeconds(10);
        }
        //this.pickupDeadline = LocalDateTime.now().plusDays(5); // 5일 뒤로 설정
    }
}
