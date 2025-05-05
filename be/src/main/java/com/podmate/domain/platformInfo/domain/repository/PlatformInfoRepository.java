package com.podmate.domain.platformInfo.domain.repository;

import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import com.podmate.domain.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformInfoRepository extends JpaRepository<PlatformInfo, Long> {
    Optional<PlatformInfo> findByUserAndPlatformName(User user, String platformName);

    List<PlatformInfo> findAllByUser(User user);

}
