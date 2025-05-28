package com.podmate.domain.pod.domain.repository;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.pod.domain.enums.PodStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PodRepository extends JpaRepository<Pod, Long> {
    List<Pod> findByPodStatus(PodStatus status);

    List<Pod> findAllByIdInAndPodStatus(List<Long> podIds, PodStatus podStatus);

    boolean existsByIdAndPodStatus(Long podId, PodStatus podStatus);

    @Query("""
        SELECT p FROM Pod p 
        JOIN FETCH p.address pa
        WHERE pa.latitude BETWEEN :minLat AND :maxLat
        AND pa.longitude BETWEEN :minLng AND :maxLng
        AND p.podStatus = 'IN_PROGRESS'
    """)
    List<Pod> findByAddressInBounds(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng
    );

    @Query(value = "SELECT * FROM pod WHERE pod_id = :id", nativeQuery = true)
    Optional<Pod> findByIdNative(@Param("id") Long id);

    Optional<Pod> findByIdAndInprogressStatus(Long podId, InprogressStatus inprogressStatus);

}
