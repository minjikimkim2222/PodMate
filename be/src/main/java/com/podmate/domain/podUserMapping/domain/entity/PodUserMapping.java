package com.podmate.domain.podUserMapping.domain.entity;

import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //private Order orderFormId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IsApproved isApproved;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PodRole podRole;

}
