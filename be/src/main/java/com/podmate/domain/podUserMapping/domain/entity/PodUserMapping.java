package com.podmate.domain.podUserMapping.domain.entity;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pod_user_mapping")
public class PodUserMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pod_id")
    private Pod pod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_form_id")
    private OrderForm orderForm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IsApproved isApproved;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PodRole podRole;

    @Column(nullable = true)
    private Integer groupBuyQuantity;

    @Builder
    private PodUserMapping(Pod pod, User user, IsApproved isApproved, PodRole podRole, Integer groupBuyQuantity) {
        this.pod = pod;
        this.user = user;
        this.isApproved = isApproved;
        this.podRole = podRole;
        this.groupBuyQuantity = groupBuyQuantity;
    }


    // 팩토리 메서드 패턴 추가
    public static PodUserMapping updatePodUserMappingForOrderForm(Pod pod, User user, OrderForm orderForm, IsApproved isApproved, PodRole podRole) {
        PodUserMapping mapping = new PodUserMapping();
        mapping.pod = pod;
        mapping.user = user;
        mapping.orderForm = orderForm;
        mapping.isApproved = isApproved;
        mapping.podRole = podRole;

        return mapping;
    }

}
