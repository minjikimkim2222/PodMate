package com.podmate.domain.pod.domain.entity;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "pod")
@Getter
public class Pod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pod_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String podName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PodType podType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PodStatus podStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InprogressStatus inprogressStatus;

//    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)    //MINIMUM인 경우만.
    private Platform platform = Platform.UNKNOWN;

    @Column(nullable = false)
    private int currentAmount;

    @Column(nullable = false)
    private int goalAmount;

//    @Column(nullable = false)
    private String description;

    @Column(nullable = true) //GROUP_BUY인 경우만.
    private String itemUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private Integer totalAmount;    //필요한게 맞는지 아닌지..

    private Integer unitQuantity;

    private Integer unitPrice;

    @Builder
    private Pod(String podName, PodType podType, PodStatus podStatus, InprogressStatus inprogressStatus, LocalDate deadline, Platform platform,
                int currentAmount, int goalAmount, String description, Address address, String itemUrl, int totalAmount, int unitQuantity, int unitPrice) {
        this.podName = podName;
        this.podType = podType;
        this.podStatus = podStatus;
        this.inprogressStatus = inprogressStatus;
        this.deadline = deadline;
        this.platform = platform;
        this.currentAmount = currentAmount;
        this.goalAmount = goalAmount;
        this.description = description;
        this.address = address;
        this.itemUrl = itemUrl;
        this.totalAmount = totalAmount;
        this.unitQuantity = unitQuantity;
        this.unitPrice = unitPrice;
    }

    public static Pod createMinimumPod(String name, Platform platform, LocalDate deadline, int goalAmount, String description, Address address) {
        return Pod.builder()
                .podName(name)
                .podType(PodType.MINIMUM)
                .podStatus(PodStatus.IN_PROGRESS)
                .inprogressStatus(InprogressStatus.PENDING_ACCEPTANCE)
                .deadline(deadline)
                .platform(platform)
                .currentAmount(0)
                .goalAmount(goalAmount)
                .description(description)
                .address(address)
                .build();
    }

    public static Pod createGroupBuyPod(String name, LocalDate deadline, int goalAmount, String description, Address address,String itemUrl, int unitQuantity, int unitPrice) {
        return Pod.builder()
                .podName(name)
                .podType(PodType.GROUP_BUY)
                .podStatus(PodStatus.IN_PROGRESS)
                .inprogressStatus(InprogressStatus.PENDING_ACCEPTANCE)
                .deadline(deadline)
                .currentAmount(0)
                .goalAmount(goalAmount)
                .description(description)
                .address(address)
                .itemUrl(itemUrl)
                .unitQuantity(unitQuantity)
                .unitPrice(unitPrice)
                .build();
    }
}
