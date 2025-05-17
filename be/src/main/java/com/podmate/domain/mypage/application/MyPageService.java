package com.podmate.domain.mypage.application;

import com.podmate.domain.delivery.exception.DeliveryNotFoundException;
import com.podmate.domain.delivery.domain.entity.Delivery;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import com.podmate.domain.delivery.domain.reposiotry.DeliveryRepository;
import com.podmate.domain.mypage.dto.MyPageRequestDto;
import com.podmate.domain.mypage.exception.PodStatusNotRecruitingException;
import com.podmate.domain.orderForm.converter.OrderFormConverter;
import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.orderForm.domain.entity.OrderItem;
import com.podmate.domain.orderForm.domain.repository.OrderFormRepository;
import com.podmate.domain.orderForm.domain.repository.OrderItemRepository;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto;
import com.podmate.domain.orderForm.exception.OrderFormNotFoundException;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.InprogressStatus;
import com.podmate.domain.pod.domain.enums.PodStatus;
import com.podmate.domain.pod.domain.enums.PodType;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.pod.exception.PodStatusMismatchException;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.pod.exception.PodTypeNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.podmate.domain.pod.converter.PodConverter.*;
import static com.podmate.domain.pod.domain.enums.InprogressStatus.RECRUITING;
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
    private final OrderFormRepository orderFormRepository;
    private final OrderItemRepository orderItemRepository;

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
            List<PodResponseDto.MMinimumPodMember> podMembers = members.stream()
                    .map(member -> {
                        PodUserMapping mapping = podUserMappingRepository
                                .findByPod_IdAndUser_IdAndPodRole(podId, member.getId(), POD_MEMBER)
                                .orElseThrow(PodUserMappingNotFoundException::new);
                        OrderForm orderForm = mapping.getOrderForm();

                        return buildMinimumPodMemberResponseDto(member, orderForm, mapping.getIsApproved());
                    }).toList();

            return PodResponseDto.MinimumPodMembers.builder()
                    .podId(pod.getId())
                    .podMembers(podMembers)
                    .build();

        } else if (pod.getPodType() == PodType.GROUP_BUY) {
            List<PodResponseDto.PodMember> podMembers = members.stream()
                    .map(member -> {
                        PodUserMapping mapping = podUserMappingRepository
                                .findByPod_IdAndUser_IdAndPodRole(podId, member.getId(), POD_MEMBER)
                                .orElseThrow(PodUserMappingNotFoundException::new);
                        return buildGroupBuyPodMemberResponseDto(member, mapping);
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

    public OrderFormResponseDto.OrderFormDetailDto getMyPodOrderFrom(Long podId, Long memberId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(pod.getId(), user.getId())){
            throw new PodUserMappingNotFoundException();
        }
        PodUserMapping podUserMapping = podUserMappingRepository.findByPod_IdAndUser_IdAndPodRole(pod.getId(), member.getId(), POD_MEMBER)
                .orElseThrow(() -> new PodNotFoundException());
        OrderForm orderForm = podUserMapping.getOrderForm();
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderForm(orderForm);

        return OrderFormConverter.toDetailResponseDto(orderItems, orderForm.getTotalAmount());
    }

    public OrderFormResponseDto.OrderFormDetailDto getMyOrderFrom(Long podId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());
        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(pod.getId(), user.getId())){
            throw new PodUserMappingNotFoundException();
        }
        PodUserMapping podUserMapping = podUserMappingRepository.findByPod_IdAndUser_IdAndPodRole(pod.getId(), user.getId(), POD_MEMBER) //위 로직과 이 부분만 다름(member.getId<->user.getId)
                .orElseThrow(() -> new PodNotFoundException());
        OrderForm orderForm = podUserMapping.getOrderForm();
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderForm(orderForm);

        return OrderFormConverter.toDetailResponseDto(orderItems, orderForm.getTotalAmount());
    }

    public void addTrackingNum(MyPageRequestDto.TrackingNumRequestDto request, Long podId, Long userId){
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
        podUserMappingRepository.findByPod_IdAndUser_IdAndPodRole(pod.getId(), user.getId(), PodRole.POD_MEMBER)
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
                            .map(mapping -> mapping.getReviewOption().getOptionText().getReviewText()) // enum 이름 기준
                            .collect(Collectors.toList());

                    return ReviewResponseDto.MyReview.builder()
                            .recipient(recipientDto)
                            .optionTexts(optionTexts)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ReviewResponseDto.AboutMeReview getReceivedReviews(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        // user가 받은 리뷰들
        List<Review> receivedReviews = reviewRepository.findAllByRecipient(user);

        // 각 리뷰에서 ReviewOption을 찾아 OptionText 기준으로 그룹화
        Map<String, Long> grouped = receivedReviews.stream()
                .flatMap(review -> reviewOptionMappingRepository.findAllByReviewId(review.getId()).stream())
                .map(mapping -> mapping.getReviewOption().getOptionText().getReviewText())  // optionText 가져오기
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));  // optionText별 개수 셈

        // 받은 옵션들에 대해 ReceivedReview 객체 생성
        List<ReviewResponseDto.ReceivedReview> reviews = grouped.entrySet().stream()
                .map(entry -> new ReviewResponseDto.ReceivedReview(
                        entry.getKey(),          // optionText
                        entry.getValue().intValue() // amount
                ))
                .collect(Collectors.toList());

        ReviewResponseDto.Profile profile = ReviewResponseDto.Profile.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImage())
                .mannerScore(user.getMannerScore())
                .build();

        return ReviewResponseDto.AboutMeReview.builder()
                .profile(profile)
                .reviews(reviews)
                .build();
    }

    public void podAcceptReject (MyPageRequestDto.IsApprovedStatusRequestDto request, Long podId, Long memberId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException());

        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        if (pod.getInprogressStatus() != RECRUITING) {
            throw new PodStatusNotRecruitingException();
        }

        PodUserMapping mapping = podUserMappingRepository
                .findByPod_IdAndUser_IdAndPodRole(podId, member.getId(), POD_MEMBER)
                .orElseThrow(PodUserMappingNotFoundException::new);

        IsApproved isAccepted = IsApproved.valueOf(request.getIsApprovedStatus());
        mapping.updatePodUserMappingIsApproved(isAccepted);

        if (isAccepted == IsApproved.ACCEPTED) {
            if (pod.getPodType() == PodType.MINIMUM) {
                OrderForm orderForm = mapping.getOrderForm();
                if (orderForm == null) {
                    throw new OrderFormNotFoundException();
                }
                pod.increaseCurrentAmount(orderForm.getTotalAmount());
            } else if (pod.getPodType() == PodType.GROUP_BUY) {
                pod.increaseCurrentAmount(1);
            }
        }

        // 목표 수량 도달 시 상태 변경
        if (pod.getCurrentAmount() >= pod.getGoalAmount()) {
            pod.updateInprogressStatus(InprogressStatus.PENDING_ORDER);
        }
    }
}
