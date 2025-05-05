package com.podmate.domain.review.application;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.review.domain.entity.Review;
import com.podmate.domain.review.domain.entity.ReviewOption;
import com.podmate.domain.review.domain.entity.ReviewOptionMapping;
import com.podmate.domain.review.domain.enums.OptionText;
import com.podmate.domain.review.domain.repository.ReviewOptionMappingRepository;
import com.podmate.domain.review.domain.repository.ReviewOptionRepository;
import com.podmate.domain.review.domain.repository.ReviewRepository;
import com.podmate.domain.review.dto.ReviewRequestDto;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.domain.review.exception.InvalidReviewOptionTextException;
import com.podmate.domain.review.exception.ReviewOptionNotFoundException;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewOptionMappingRepository reviewOptionMappingRepository;
    private final ReviewOptionRepository reviewOptionRepository;

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
                            .userId(reviewTarget.getId())
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

    public Long createReview(Long recipientId, ReviewRequestDto requestDto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(()-> new UserNotFoundException());
        Pod pod = podRepository.findById(requestDto.getPodId())
                .orElseThrow(() -> new PodNotFoundException());

        //리뷰 저장
        Review review = Review.builder()
                .writer(user)
                .recipient(recipient)
                .pod(pod)
                .build();
        Review savedReview = reviewRepository.save(review);

        // OptionText -> ReviewOption 매핑
        List<ReviewOption> reviewOptions = requestDto.getOptions().stream()
                .map(text -> Arrays.stream(OptionText.values())
                        .filter(opt -> opt.name().equals(text)) // Enum name으로 정확히 비교
                        .findFirst()
                        .map(optionText -> reviewOptionRepository.findByOptionText(optionText)  // OptionText enum 사용
                                .orElseThrow(() -> new InvalidReviewOptionTextException()))
                        .orElseThrow(() -> new ReviewOptionNotFoundException()))
                .collect(Collectors.toList());

        //매핑 저장
        List<ReviewOptionMapping> mappings = reviewOptions.stream()
                .map(option -> new ReviewOptionMapping(review, option))
                .collect(Collectors.toList());
        reviewOptionMappingRepository.saveAll(mappings);

        return savedReview.getId();
    }
}
