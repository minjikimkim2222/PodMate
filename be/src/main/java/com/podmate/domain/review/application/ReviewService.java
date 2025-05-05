package com.podmate.domain.review.application;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.review.domain.repository.ReviewRepository;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final ReviewRepository reviewRepository;

    public ReviewResponseDto.ReviewTarget getReviewTarget(Long podId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(podId, userId)){
            throw new PodUserMappingNotFoundException();
        }
        List<PodUserMapping> allByPodIdAndUserId = podUserMappingRepository.findAllByPod_Id(pod.getId());

        List<ReviewResponseDto.PodMember> podMembers = allByPodIdAndUserId.stream()
                .filter(mapping -> !mapping.getUser().getId().equals(user.getId())) // 같은 ID는 제외
                .map(mapping -> {
                    User reviewTarget = mapping.getUser();
                    return ReviewResponseDto.PodMember.builder()
                            .userID(reviewTarget.getId())
                            .nickname(reviewTarget.getNickname())
                            .profileImageUrl(reviewTarget.getProfileImage())
                            .build();
                })
                .collect(Collectors.toList());

        return ReviewResponseDto.ReviewTarget.builder()
                .podId(pod.getId())
                .podType(pod.getPodType().name())
                .podMembers(podMembers)
                .build();
    }
}
