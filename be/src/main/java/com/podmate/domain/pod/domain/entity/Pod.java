package com.podmate.domain.pod.domain.entity;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.pod.domain.enums.Platform;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private Integer totalAmount;    //전체 개수

    private Integer unitQuantity;   //단위 개수

    private Integer unitPrice;      //단위 가격

    private String depositAccountBank;     // 은행명

    private String depositAccountNumber;   // 계좌번호

    private String depositAccountHolder;   // 예금주

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
                .inprogressStatus(InprogressStatus.RECRUITING)
                .deadline(deadline)
                .platform(platform)
                .currentAmount(0)
                .goalAmount(goalAmount)
                .description(description)
                .address(address)
                .build();
    }

    public static Pod createGroupBuyPod(String name, LocalDate deadline, String description, Address address, String itemUrl, int totalAmount, int unitQuantity, int unitPrice) {
        return Pod.builder()
                .podName(name)
                .podType(PodType.GROUP_BUY)
                .podStatus(PodStatus.IN_PROGRESS)
                .inprogressStatus(InprogressStatus.RECRUITING)
                .deadline(deadline)
                .currentAmount(0)
                .goalAmount(totalAmount/unitQuantity)
                .description(description)
                .address(address)
                .itemUrl(itemUrl)
                .totalAmount(totalAmount)
                .unitQuantity(unitQuantity)
                .unitPrice(unitPrice)
                .platform(Platform.UNKNOWN)
                .build();
    }

    public void updateInprogressStatus(InprogressStatus status) {
        this.inprogressStatus = status;
    }

    public void increaseCurrentAmount(int currentAmount) {
        this.currentAmount += currentAmount;
    }

    public void updatePodStatus(PodStatus status) {
        this.podStatus = status;
    }

    public void updateDepositAccount (String depositAccountBank, String depositAccountHolder, String depositAccountNumber) {
        this.depositAccountBank = depositAccountBank;
        this.depositAccountNumber = depositAccountNumber;
        this.depositAccountHolder = depositAccountHolder;
    }
}
