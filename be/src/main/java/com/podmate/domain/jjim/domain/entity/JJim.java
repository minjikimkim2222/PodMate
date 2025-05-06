package com.podmate.domain.jjim.domain.entity;

import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "jjim")
public class JJim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pod_id")
    private Pod pod;

    public JJim(User user, Pod pod) {
        this.user = user;
        this.pod = pod;
    }
}
