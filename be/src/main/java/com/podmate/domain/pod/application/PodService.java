package com.podmate.domain.pod.application;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.address.domain.repository.AddressRepository;
import com.podmate.domain.address.dto.AddressRequestDto;
import com.podmate.domain.address.exception.AddressNotFoundException;
import com.podmate.domain.delivery.exception.DeliveryNotFoundException;
import com.podmate.domain.delivery.exception.ShippingMismatchException;
import com.podmate.domain.delivery.domain.entity.Delivery;
import com.podmate.domain.delivery.domain.enums.DeliveryStatus;
import com.podmate.domain.delivery.domain.reposiotry.DeliveryRepository;
import com.podmate.domain.jjim.domain.entity.JJim;
import com.podmate.domain.jjim.domain.repository.JJimRepository;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.enums.*;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.dto.PodRequestDto;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.pod.exception.InvalidStatusException;
import com.podmate.domain.pod.exception.PendingOrderMismatchException;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.podUserMapping.domain.enums.PodRole;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.podmate.domain.pod.converter.PodConverter.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PodService {

    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final JJimRepository jjimRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final AddressRepository addressRepository;
    private final DeliveryRepository deliveryRepository;

    private static final int EARTH_RADIUS_KM = 6371; // 지구 반지름 (단위: km)

    public List<PodResponse> getPodList(Long userId, SortBy sortBy, List<String> platforms, String podType){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException());
        double userLat = user.getAddress().getLatitude();
        double userLon = user.getAddress().getLongitude();

        List<Pod> allPods = podRepository.findByPodStatus(PodStatus.IN_PROGRESS);
        // 필터링: 플랫폼
        if (platforms != null && !platforms.isEmpty()) {
            allPods = allPods.stream()
                    .filter(pod -> platforms.contains(pod.getPlatform().toString()))
                    .collect(Collectors.toList());
        }
        // 필터링: 팟 타입
        if (podType != null) {
            allPods = allPods.stream()
                    .filter(pod -> pod.getPodType().toString().equalsIgnoreCase(podType))
                    .collect(Collectors.toList());
        }

        Set<Long> jjimPodIds = getJJimPodIds(userId);
        // 거리 필터
        List<Pod> filteredPods = allPods.stream()
                .filter(pod -> isWithinMaxDistance(userLat, userLon, pod))
                .collect(Collectors.toList());

        // 정렬
        if (sortBy == null || sortBy == SortBy.DISTANCE) {
            filteredPods.sort(Comparator.comparingDouble(pod -> calculateDistance(userLat, userLon, pod)));
        } else if (sortBy == SortBy.DEADLINE) {
            filteredPods.sort(Comparator.comparing(Pod::getDeadline));
        }

        return filteredPods.stream()
                .map(pod -> mapToPodResponseDto(pod, jjimPodIds))
                .collect(Collectors.toList());

        //정렬 필터 적용 전 주변 팟 리스트 가져오기 코드
//        return allPods.stream()
//                .filter(pod -> isWithinMaxDistance(userLat, userLon, pod))
//                .map(pod -> mapToPodResponseDto(pod, jjimPodIds))
//                .collect(Collectors.toList());

        //하나하나 디버깅 해봄 (정렬 필터 적용 전)
//        List<Pod> filteredPods = allPods.stream()
//                .filter(pod -> {
//                    boolean within = isWithinMaxDistance(userLat, userLon, pod);
//                    if (!within) {
//                        System.out.println("거리 초과 pod: " + pod.getId());
//                    }
//                    return within;
//                })
//                .collect(Collectors.toList());
//
//        System.out.println("거리 필터링 통과 pod 수: " + filteredPods.size());
//
//        List<PodResponse> responses = filteredPods.stream()
//                .map(pod -> {
//                    PodResponse dto = mapToPodResponseDto(pod, jjimPodIds);
//                    System.out.println("매핑된 pod: " + pod.getId() + " -> dto");
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        System.out.println("최종 반환할 pod 수: " + responses.size());
//        return responses;
    }

    private Set<Long> getJJimPodIds(Long userId) {
        List<JJim> jjims = jjimRepository.findAllByUserId(userId);
        return jjims.stream()
                .map(jjim -> jjim.getPod().getId())
                .collect(Collectors.toSet());
    }

    private boolean isWithinMaxDistance(double userLat, double userLon, Pod pod) {
        double distance = calculateDistance(userLat, userLon, pod);
//        System.out.println("Pod ID: " + pod.getId() + ", 거리: " + distance + "km");
        return distance <= 1.0;  // maxDistanceKm가 1로 고정되어 있으므로 하드코딩
    }

    private PodResponse mapToPodResponseDto(Pod pod, Set<Long> jjimPodIds) {
        boolean isJJim = jjimPodIds.contains(pod.getId());

        if (pod.getPodType() == PodType.MINIMUM) {
            return buildMinimumPodResponseDto(pod, isJJim);
        } else if (pod.getPodType() == PodType.GROUP_BUY) {
            return buildGroupBuyPodResponseDto(pod, isJJim);
        }

        return null;  // PodType이 두 가지 외에는 처리되지 않음
    }

    private PodResponse mapToPodResponseDtoForMap(Pod pod, Set<Long> jjimPodIds, boolean includeAddress) {
        boolean isJJim = jjimPodIds.contains(pod.getId());

        if (pod.getPodType() == PodType.MINIMUM) {
            return buildMinimumPodResponseDtoForMap(pod, isJJim, true);
        } else if (pod.getPodType() == PodType.GROUP_BUY) {
            return buildGroupBuyPodResponseDtoForMap(pod, isJJim, true);
        }

        return null;  // PodType이 두 가지 외에는 처리되지 않음
    }

    //사용자 주변 팟 거리 계산 메서드
    private double calculateDistance(double lat1, double lon1, Pod pod) {
        double lat2 = pod.getAddress().getLatitude();
        double lon2 = pod.getAddress().getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public PodResponse getPodDetail(Long userId, Long podId){
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        PodUserMapping mapping = podUserMappingRepository.findByPodAndPodRole(pod, PodRole.POD_LEADER)
                .orElseThrow(() -> new PodUserMappingNotFoundException());

        User podLeader = mapping.getUser();
        boolean isJJim = jjimRepository.existsByUserIdAndPodId(userId, podId);

        PodResponseDto.PodLeader podLeaderDto = new PodResponseDto.PodLeader(
                podLeader.getNickname(),
                podLeader.getProfileImage(),
                pod.getDescription()
        );

        if (pod.getPodType() == PodType.MINIMUM){
            return buildMinimumDetailResponseDto(pod, isJJim, podLeaderDto);
        } else if (pod.getPodType() == PodType.GROUP_BUY){
            return buildGroupBuyDetailResponseDto(pod, isJJim, podLeaderDto);
        }
        return null;
    }

    public Long createMinimum(PodRequestDto.MininumRequestDto request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException());

        Platform platform = Platform.fromDisplayName(request.getPlatform()); // String -> Enum

        Pod pod = Pod.createMinimumPod(request.getPodName(), platform, request.getDeadline(),
                                        request.getGoalAmount(), request.getDescription(), address);

        Pod savedPod = podRepository.save(pod);

        // 팟 생성자 → podUserMapping에 leader로 추가
        PodUserMapping mapping = PodUserMapping.builder()
                .pod(savedPod)
                .user(user)
                .isApproved(IsApproved.ACCEPTED)
                .podRole(PodRole.POD_LEADER)
                .build();

        podUserMappingRepository.save(mapping);

        return savedPod.getId();
    }

    public Long createGroupBuy(PodRequestDto.GroupBuyRequestDto request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException());

        Pod pod = Pod.createGroupBuyPod(request.getPodName(), request.getDeadline(), request.getDescription(),
                                        address, request.getItemUrl(), request.getTotalAmount(), request.getUnitQuantity(), request.getUnitPrice());

        Pod savedPod = podRepository.save(pod);

        // 팟 생성자 → podUserMapping에 leader로 추가
        PodUserMapping mapping = PodUserMapping.builder()
                .pod(savedPod)
                .user(user)
                .isApproved(IsApproved.ACCEPTED)
                .podRole(PodRole.POD_LEADER)
                .build();

        podUserMappingRepository.save(mapping);

        return savedPod.getId();
    }

    public Long createAddress(AddressRequestDto.AddressUpdateRequest request){
        Address address = Address.builder()
                .roadAddress(request.getRoadAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        Address savedAddress = addressRepository.save(address);

        return savedAddress.getId();
    }

    public void joinPod(PodRequestDto.GroupBuyJoinRequestDto request, Long podId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(pod.getId(), user.getId())){
            throw new PodUserMappingNotFoundException();
        }

        PodUserMapping mapping = PodUserMapping.builder()
                .pod(pod)
                .user(user)
                .isApproved(IsApproved.PENDING)
                .podRole(PodRole.POD_MEMBER)
                .groupBuyQuantity(request.getQuantity())
                .build();

        podUserMappingRepository.save(mapping);
    }

    public void updatePodStatus(Long podId, PodRequestDto.ChangingInprogressStatus request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());
        Delivery delivery = deliveryRepository.findByPod_Id(pod.getId())
                .orElseThrow(() -> new DeliveryNotFoundException());

        if (!podUserMappingRepository.existsByPod_IdAndUser_IdAndPodRole(pod.getId(), user.getId(), PodRole.POD_LEADER)) {
            throw new PodUserMappingNotFoundException();
        }

        InprogressStatus currentStatus = pod.getInprogressStatus();
        String nextStatus = request.getStatus();

        // 상태 전이 조건 검사
        if ("ORDER_COMPLETED".equals(nextStatus)) {
            if (currentStatus != InprogressStatus.PENDING_ORDER) {
                throw new PendingOrderMismatchException();
            }
            pod.updateInprogressStatus(InprogressStatus.ORDER_COMPLETED);
        }
        else if("DELIVERED".equals(nextStatus)) {
            if (delivery.getDeliveryStatus() != DeliveryStatus.SHIPPING){
                throw new ShippingMismatchException();
            }
            delivery.updateDeliveryStatus(DeliveryStatus.DELIVERED);
        }
        else{
            throw new InvalidStatusException();
        }
    }


    // 위경도 범위로 팟 조회
    public List<PodResponse> getPodsInBounds(
            double lat1, double lng1, double lat2, double lng2, Long userId
    ) {
        // 1. 위도/경도 정렬
        double minLat = Math.min(lat1, lat2);
        double maxLat = Math.max(lat1, lat2);
        double minLng = Math.min(lng1, lng2);
        double maxLng = Math.max(lng1, lng2);

        List<Pod> pods = podRepository.findByAddressInBounds(
                minLat, maxLat, minLng, maxLng
        );

        Set<Long> jJimPodIds = getJJimPodIds(userId);

        return pods.stream()
                .map(pod -> mapToPodResponseDtoForMap(pod, jJimPodIds, true))
                .collect(Collectors.toList());
    }



    //15초마다 배송 pickupDeadline 완료된거 있는지 점검 -> log가 자주 떠서 1분으로 변경
    @Scheduled(fixedRate = 60000)
    public void checkAndCompletePods() {
        // 1. 배송 완료 상태이고 픽업 마감 기한이 지난 Delivery들 조회
        List<Delivery> deliveries = deliveryRepository
                .findAllByDeliveryStatusAndPickupDeadlineBefore(DeliveryStatus.DELIVERED, LocalDateTime.now());

        for (Delivery delivery : deliveries) {
            Pod pod = delivery.getPod();
            if (pod != null && pod.getPodStatus() != PodStatus.COMPLETED) {
                pod.updatePodStatus(PodStatus.COMPLETED);
            }
        }

    }
}