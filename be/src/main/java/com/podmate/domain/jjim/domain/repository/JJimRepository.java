package com.podmate.domain.jjim.domain.repository;

import com.podmate.domain.jjim.domain.entity.JJim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JJimRepository extends JpaRepository<JJim, Long> {
    List<JJim> findAllByUserId(Long userId);
    boolean existsByUserIdAndPodId(Long userId, Long podId);
}
