package com.podmate.domain.mypage.application;

import com.podmate.domain.delivery.domain.entity.Delivery;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import com.podmate.domain.delivery.domain.reposiotry.DeliveryRepository;
import com.podmate.domain.mypage.dto.MyPageRequestDto;
import com.podmate.domain.pod.converter.PodConverter;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.pod.exception.PodTypeNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodLeaderNotFoundException;
import com.podmate.domain.podUserMapping.exception.PodLeaderUserMismatchException;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.podmate.domain.pod.converter.PodConverter.*;
import static com.podmate.domain.pod.converter.PodConverter.buildMinimumPodResponseDto;
import static com.podmate.domain.podUserMapping.domain.enums.PodRole.POD_MEMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final PodRepository podRepository;
    private final DeliveryRepository deliveryRepository;

    public List<PodResponse> getInprogressMyPods(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());

        List<Long> podIds = podUserMappingRepository.findPodIdsByUserIdAndRole(user.getId(), PodRole.POD_LEADER);

        List<Pod> pods = podRepository.findAllByIdInAndPodStatus(podIds, PodStatus.IN_PROGRESS);

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

    public PodResponse getInprogressMembers(Long podId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        Long leaderUserId = podUserMappingRepository.findLeaderUserIdByPodId(podId)
                .orElseThrow(()-> new PodLeaderNotFoundException());

        if(!leaderUserId.equals(user.getId())) {
            throw new PodLeaderUserMismatchException();
        }
        Pod pod = podRepository.findById(podId).orElseThrow(() -> new PodNotFoundException());

        List<User> members = podUserMappingRepository.findMembersByPodId(podId);

        if (pod.getPodType() == PodType.MINIMUM) {
            List<PodResponseDto.MinimumPodMember> podMembers = new ArrayList<>();
            for (User member : members) {
                PodUserMapping podUserMapping = podUserMappingRepository.findByPodIdAndUserIdAndPodRole(podId, member.getId(), POD_MEMBER)
                        .orElseThrow(() -> new PodUserMappingNotFoundException());
                podMembers.add(buildMinimumPodMemberResponseDto(member, podUserMapping.getIsApproved()));
            }
            return PodResponseDto.MinimumPodMembers.builder()
                    .podId(pod.getId())
                    .podMembers(podMembers)
                    .build();
        } else if (pod.getPodType() == PodType.GROUP_BUY) {
            List<PodResponseDto.PodMember> podMembers = new ArrayList<>();
            for (User member : members) {
                PodUserMapping podUserMapping = podUserMappingRepository.findByPodIdAndUserIdAndPodRole(podId, member.getId(), POD_MEMBER)
                        .orElseThrow(() -> new PodUserMappingNotFoundException());
                podMembers.add(buildPodMemberResponseDto(member, podUserMapping.getIsApproved()));
            }
            return PodResponseDto.GroupBuyPodMembers.builder()
                    .podId(pod.getId())
                    .itemUrl(pod.getItemUrl())
                    .podMembers(podMembers)
                    .build();
        }else {
            throw new PodTypeNotFoundException();
        }
    }

    public void addTrackingNum(MyPageRequestDto request, Long podId, Long userId){
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId).orElseThrow(() -> new PodNotFoundException());

        Delivery delivery = Delivery.builder()
                .pod(pod)
                .tackingNum(request.getTrackingNum())
                .courierCompany(request.getCourierCompany())
                .pickupDeadline(LocalDate.now().plusDays(5)) //오늘로부터 5일 뒤
                .deliveryStatus(DeliveryStatus.SHIPPING)
                .build();

        deliveryRepository.save(delivery);
    }

    public List<PodResponse> getCompletedMyPods(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());

        List<Long> podIds = podUserMappingRepository.findPodIdsByUserIdAndRole(userId, PodRole.POD_LEADER);

        List<Pod> pods = podRepository.findAllByIdInAndPodStatus(podIds, PodStatus.COMPLETED);

        // Pod 엔티티 -> PodResponse DTO 변환
        return pods.stream()
                .map(pod -> {
                    if (pod.getPodType() == PodType.MINIMUM) {
                        return buildMinimumCompletedResponseDto(pod);
                    } else {
                        return buildGroupBuyCompletedResponseDto(pod);
                    }
                })
                .collect(Collectors.toList());
    }
}
