package com.podmate.domain.review.api;

import com.podmate.domain.review.application.ReviewService;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{podId}")
    public BaseResponse<ReviewResponseDto.ReviewTarget> getReviewTargets(@PathVariable Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        ReviewResponseDto.ReviewTarget reviewTarget = reviewService.getReviewTarget(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, reviewTarget);
    }
}
