package com.podmate.domain.orderForm.domain.entity;

import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orderform")
@Getter
public class OrderForm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_form_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_amount")
    private int totalAmount; // 총금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Builder
    public OrderForm(User user, int totalAmount, Platform platform){
        this.user = user;
        this.totalAmount = totalAmount;
        this.platform = platform;
    }

}
