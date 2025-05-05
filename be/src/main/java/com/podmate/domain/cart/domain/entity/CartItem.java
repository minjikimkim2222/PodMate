package com.podmate.domain.cart.domain.entity;

import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
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
@Table(name = "cart_item")
@Getter
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_info_id")
    private PlatformInfo platformInfo;

    @Column(name = "item_name")
    private String itemName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, name = "item_url")
    private String itemUrl;

    @Column(name = "option_text")
    private String optionText;

    private int price;

    @Builder
    public CartItem(PlatformInfo platformInfo, String itemName, int quantity, String itemUrl, String optionText, int price){
        this.platformInfo = platformInfo;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemUrl = itemUrl;
        this.optionText = optionText;
        this.price = price;
    }


}
