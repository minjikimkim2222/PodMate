package com.podmate.domain.pod.domain.repository;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.PodStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodRepository extends JpaRepository<Pod, Long> {
    List<Pod> findByPodStatus(PodStatus status);
}
