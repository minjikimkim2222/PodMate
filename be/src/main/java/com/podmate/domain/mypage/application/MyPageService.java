package com.podmate.domain.mypage.application;

import com.podmate.domain.pod.converter.PodConverter;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.podmate.domain.pod.converter.PodConverter.*;
import static com.podmate.domain.pod.converter.PodConverter.buildMinimumPodResponseDto;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final PodRepository podRepository;

    public List<PodResponse> getInprogressMyPods(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());

        List<Long> podIds = podUserMappingRepository.findPodIdsByUserIdAndRole(userId, PodRole.POD_LEADER);

        List<Pod> pods = podRepository.findAllByIdIn(podIds);

        // Pod 엔티티 -> PodResponse DTO 변환
        return pods.stream()
                .map(pod -> {
                    if (pod.getPodType() == PodType.MINIMUM) {
                        return buildMinimumStatusResponseDto(pod);
                    } else {
                        return buildGroupBuyStatusResponseDto(pod);
                    }
                })
                .collect(Collectors.toList());
    }


}
