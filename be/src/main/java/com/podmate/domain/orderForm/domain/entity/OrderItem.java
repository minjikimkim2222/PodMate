package com.podmate.domain.orderForm.domain.entity;

import com.podmate.domain.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "order_item")
@Getter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_form_id")
    private OrderForm orderForm;

    @Column(name = "item_name")
    private String itemName;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "item_url", nullable = false)
    private String itemUrl;

    @Column(name = "option_text")
    private String optionText;

    private int price;

    @Builder
    public OrderItem(OrderForm orderForm, String itemName, int quantity, String itemUrl, String optionText, int price){
        this.orderForm = orderForm;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemUrl = itemUrl;
        this.optionText = optionText;
        this.price = price;
    }
}
