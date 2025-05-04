package com.podmate.domain.podUserMapping.domain.repository;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PodUserMappingRepository extends JpaRepository<PodUserMapping, Long> {

    Optional<PodUserMapping> findByPodAndPodRole(Pod pod, PodRole podRole);

    //podId, userId, podRole 일치하는 PodUserMapping 하나 조회
    Optional<PodUserMapping> findByPodIdAndUserIdAndPodRole(Long podId, Long userId, PodRole podRole);

//    Optional<PodUserMapping> findByPodIdAndUserId(Long podId, Long userId);

    //userId와 podRole로 repository에서 podId list 조회
    @Query("SELECT pum.pod.id FROM PodUserMapping pum WHERE pum.user.id = :userId AND pum.podRole = :podRole")
    List<Long> findPodIdsByUserIdAndRole(@Param("userId") Long userId, @Param("podRole") PodRole podRole);

    //podId의 podRole이 POD_LEADER인 userId를 조회
    @Query("SELECT pum.user.id FROM PodUserMapping pum WHERE pum.pod.id = :podId AND pum.podRole = 'POD_LEADER'")
    Optional<Long> findLeaderUserIdByPodId(@Param("podId") Long podId);

    //podId의 podRole이 POD_MEMBER인 user list 조회
    @Query("SELECT pum.user FROM PodUserMapping pum WHERE pum.pod.id = :podId AND pum.podRole = 'POD_MEMBER'")
    List<User> findMembersByPodId(@Param("podId") Long podId);

}
