package com.podmate.domain.podUserMapping.domain.repository;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PodUserMappingRepository extends JpaRepository<PodUserMapping, Long> {
    Optional<PodUserMapping> findByPodAndPodRole(Pod pod, PodRole podRole);
}
