package com.podmate.domain.review.api;

import com.podmate.domain.review.application.ReviewService;
import com.podmate.domain.review.dto.ReviewRequestDto;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //후기 대상자 선택
    @GetMapping("/{podId}")
    public BaseResponse<ReviewResponseDto.ReviewTarget> getReviewTargets(@PathVariable Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        ReviewResponseDto.ReviewTarget reviewTarget = reviewService.getReviewTarget(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, reviewTarget);
    }

    //후기 등록
    @PostMapping("/{recipientId}")
    public BaseResponse<Long> createReview(@RequestBody ReviewRequestDto requestDto, @PathVariable Long recipientId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long reviewId = reviewService.createReview(recipientId, requestDto, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, reviewId);
    }
}
