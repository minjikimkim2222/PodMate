package com.podmate.domain.mypage.application;

import com.podmate.domain.delivery.DeliveryNotFoundException;
import com.podmate.domain.delivery.domain.entity.Delivery;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import com.podmate.domain.delivery.domain.reposiotry.DeliveryRepository;
import com.podmate.domain.mypage.dto.MyPageRequestDto;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.pod.exception.PodStatusMismatchException;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.pod.exception.PodTypeNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodLeaderNotFoundException;
import com.podmate.domain.podUserMapping.exception.PodLeaderUserMismatchException;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.review.domain.entity.Review;
import com.podmate.domain.review.domain.repository.ReviewOptionMappingRepository;
import com.podmate.domain.review.domain.repository.ReviewRepository;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.podmate.domain.pod.converter.PodConverter.*;
import static com.podmate.domain.podUserMapping.domain.enums.PodRole.POD_LEADER;
import static com.podmate.domain.podUserMapping.domain.enums.PodRole.POD_MEMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final PodRepository podRepository;
    private final DeliveryRepository deliveryRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewOptionMappingRepository reviewOptionMappingRepository;

    public List<Pod> getPodList(Long userId, PodRole podRole, PodStatus podStatus){
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());

        List<Long> podIds = podUserMappingRepository.findPodIdsByUserIdAndRole(user.getId(), podRole);

        List<Pod> pods = podRepository.findAllByIdInAndPodStatus(podIds, podStatus);
        return pods;
    }
    public List<PodResponse> getInprogressMyPods(Long userId) {

        List<Pod> pods = getPodList(userId, POD_LEADER, PodStatus.IN_PROGRESS);

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
        return getPodMembersByStatus(podId, userId, PodStatus.IN_PROGRESS);
    }

    public PodResponse getCompletedMembers(Long podId, Long userId) {
        return getPodMembersByStatus(podId, userId, PodStatus.COMPLETED);
    }

    public PodResponse getPodMembersByStatus(Long podId, Long userId, PodStatus requiredStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Long leaderUserId = podUserMappingRepository.findLeaderUserIdByPodId(podId)
                .orElseThrow(()-> new PodLeaderNotFoundException());

        if(!leaderUserId.equals(user.getId())) {
            throw new PodLeaderUserMismatchException();
        }
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        if (!podRepository.existsByIdAndPodStatus(podId, requiredStatus)) {
            throw new PodStatusMismatchException();
        }

        List<User> members = podUserMappingRepository.findMembersByPodId(podId);

        if (pod.getPodType() == PodType.MINIMUM) {
            List<PodResponseDto.MinimumPodMember> podMembers = members.stream()
                    .map(member -> {
                        PodUserMapping mapping = podUserMappingRepository
                                .findByPodIdAndUserIdAndPodRole(podId, member.getId(), POD_MEMBER)
                                .orElseThrow(PodUserMappingNotFoundException::new);
                        return buildMinimumPodMemberResponseDto(member, mapping.getIsApproved());
                    }).toList();

            return PodResponseDto.MinimumPodMembers.builder()
                    .podId(pod.getId())
                    .podMembers(podMembers)
                    .build();

        } else if (pod.getPodType() == PodType.GROUP_BUY) {
            List<PodResponseDto.PodMember> podMembers = members.stream()
                    .map(member -> {
                        PodUserMapping mapping = podUserMappingRepository
                                .findByPodIdAndUserIdAndPodRole(podId, member.getId(), POD_MEMBER)
                                .orElseThrow(PodUserMappingNotFoundException::new);
                        return buildPodMemberResponseDto(member, mapping.getIsApproved());
                    }).toList();

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
                .trackingNum(request.getTrackingNum())
                .courierCompany(request.getCourierCompany())
                .pickupDeadline(LocalDate.now().plusDays(5)) //오늘로부터 5일 뒤
                .deliveryStatus(DeliveryStatus.SHIPPING)
                .build();

        deliveryRepository.save(delivery);
    }

    public List<PodResponse> getCompletedMyPods(Long userId) {

        List<Pod> pods = getPodList(userId, POD_LEADER, PodStatus.COMPLETED);
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

    public List<PodResponse> getInprogressJoinedPods(Long userId) {

        List<Pod> pods = getPodList(userId, POD_MEMBER, PodStatus.IN_PROGRESS);
        return pods.stream()
                .map(pod -> {
                    Delivery delivery = deliveryRepository.findByPod_Id(pod.getId())
                            .orElseThrow(() -> new DeliveryNotFoundException());

                    PodResponseDto.DeliveryDto deliveryDto = PodResponseDto.DeliveryDto.builder()
                            .courierCompany(delivery.getCourierCompany())
                            .trackingNum(delivery.getTrackingNum())
                            .build();

                    if (pod.getPodType() == PodType.MINIMUM) {
                        return buildMinimumInprogressJoinedResponseDto(pod, deliveryDto);
                    } else {
                        return buildGroupBuyInprogressJoinedResponseDto(pod, deliveryDto);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<PodResponse> getCompletedJoinedPods(Long userId) {

        List<Pod> pods = getPodList(userId, POD_MEMBER, PodStatus.COMPLETED);
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

    public PodResponse getInprogressJoinedPodInfo(Long podId, Long userId){
        if (!podRepository.existsByIdAndPodStatus(podId, PodStatus.IN_PROGRESS)) {
            throw new PodStatusMismatchException();
        }
        return getPodInfo(podId, userId);
    }
    public PodResponse getCompletedJoinedPodInfo(Long podId, Long userId){
        if (!podRepository.existsByIdAndPodStatus(podId, PodStatus.COMPLETED)) {
            throw new PodStatusMismatchException();
        }
        return getPodInfo(podId, userId);
    }

    public PodResponse getPodInfo(Long podId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());
        // user가 POD_MEMBER인지 검증
        podUserMappingRepository.findByPodIdAndUserIdAndPodRole(pod.getId(), user.getId(), PodRole.POD_MEMBER)
                .orElseThrow(() -> new PodUserMappingNotFoundException());

        PodUserMapping mapping = podUserMappingRepository.findByPodAndPodRole(pod, PodRole.POD_LEADER)
                .orElseThrow(() -> new PodUserMappingNotFoundException());

        User podLeader = mapping.getUser();

        PodResponseDto.PodLeader podLeaderDto = new PodResponseDto.PodLeader(
                podLeader.getNickname(),
                podLeader.getProfileImage(),
                pod.getDescription()
        );

        if (pod.getPodType() == PodType.MINIMUM){
            return PodResponseDto.MinimumPodInfo.builder()
                    .podId(pod.getId())
                    .podLeader(podLeaderDto)
                    .build();
        } else if (pod.getPodType() == PodType.GROUP_BUY){
            return PodResponseDto.GroupBuyPodInfo.builder()
                    .podId(pod.getId())
                    .podLeader(podLeaderDto)
                    .itemUrl(pod.getItemUrl())
                    .build();
        }
        return null;
    }

    public List<ReviewResponseDto.MyReview> getMyReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        List<Review> reviews = reviewRepository.findAllByWriter(user);

        return reviews.stream()
                .map(review -> {
                    // 리뷰 대상자 정보
                    ReviewResponseDto.PodMember recipientDto = ReviewResponseDto.PodMember.builder()
                            .userId(review.getRecipient().getId())
                            .profileImageUrl(review.getRecipient().getProfileImage())
                            .nickname(review.getRecipient().getNickname())
                            .build();

                    // 해당 리뷰에 연결된 옵션 텍스트 리스트
                    List<String> optionTexts = reviewOptionMappingRepository.findAllByReviewId(review.getId()).stream()
                            .map(mapping -> mapping.getReviewOption().getOptionText().name()) // enum 이름 기준
                            .collect(Collectors.toList());

                    return ReviewResponseDto.MyReview.builder()
                            .recipient(recipientDto)
                            .optionTexts(optionTexts)
                            .build();
                })
                .collect(Collectors.toList());

    }
}
