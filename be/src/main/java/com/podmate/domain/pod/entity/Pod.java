package com.podmate.domain.pod.entity;

import com.podmate.domain.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO :: 리뷰와 매핑된 Pod 테스트를 위해, id만 썼음
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pod")
@Getter
public class Pod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pod_id", nullable = false)
    private Long id;



}
