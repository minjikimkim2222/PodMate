package com.podmate.domain.mypage.api;

import com.podmate.domain.cart.application.CartService;
import com.podmate.domain.cart.dto.CartResponseDto;
import com.podmate.domain.mypage.application.MyPageService;
import com.podmate.domain.mypage.dto.MyPageRequestDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.review.dto.ReviewResponseDto;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final CartService cartService;

    //나의 장바구니 목록 조회
    @GetMapping("/carts/platforms")
    public BaseResponse<CartResponseDto.PlatformList> getCartList(@AuthenticationPrincipal CustomOAuth2User user) {
        CartResponseDto.PlatformList response = cartService.getCartList(user.getUserId());

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    //나의 장바구니 목록을 통한 상품 더보기 조회
    @GetMapping("/carts/platforms/{platformInfoId}/cartItems")
    public BaseResponse<CartResponseDto.CartItemList> getCartItemList(@PathVariable("platformInfoId") Long platformInfoId, @AuthenticationPrincipal CustomOAuth2User user) {
        CartResponseDto.CartItemList response = cartService.getCartItems(user.getUserId(), platformInfoId);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    //진행중인 나의 팟 리스트 조회
    @GetMapping("/inprogress/mypods")
    public BaseResponse<List<PodResponse>> getInprogressMyPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> inprogressMyPods = myPageService.getInprogressMyPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, inprogressMyPods);
    }

    //진행중인 나의 팟에 참여중인 팟원 리스트 조회
    @GetMapping("/inprogress/mypods/{podId}/podmembers")
    public BaseResponse<PodResponse> getInprogressMyPodMembers(@PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        PodResponse inprogressMembers = myPageService.getInprogressMembers(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, inprogressMembers);
    }

    //팟장의 팟원 신청 수락/거절
    @PatchMapping("/inprogress/mypods/{podId}/podmembers/{memberId}/status")
    public BaseResponse<String> podAcceptReject(@RequestBody MyPageRequestDto.IsApprovedStatusRequestDto request, @PathVariable("podId") Long podId, @PathVariable("memberId") Long memberId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        myPageService.podAcceptReject(request, podId, memberId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, "successfully changed isApproved status!");
    }

    //나의 팟에서 팟원의 주문서 상세 보기
    @GetMapping("/mypods/{podId}/{memberId}/order")
    public BaseResponse<OrderFormResponseDto.OrderFormDetailDto> getMyPodOrderFrom(@PathVariable("podId") Long podId, @PathVariable("memberId") Long memberId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        OrderFormResponseDto.OrderFormDetailDto myPodOrderFrom = myPageService.getMyPodOrderFrom(podId, memberId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, myPodOrderFrom);
    }

    //운송장 입력
    @PostMapping("/inprogress/mypods/{podId}")
    public BaseResponse<String> addTrackingNumber(@RequestBody MyPageRequestDto.TrackingNumRequestDto request, @PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        myPageService.addTrackingNum(request, podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, "successfully saved tracking number!");
    }

    //완료된 나의 팟 리스트 조회
    @GetMapping("/completed/mypods")
    public BaseResponse<List<PodResponse>> getCompletedMyPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> completedMyPods = myPageService.getCompletedMyPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, completedMyPods);
    }

    //완료된 나의 팟에 참여중인 팟원 리스트 조회
    @GetMapping("/completed/mypods/{podId}/podmembers")
    public BaseResponse<PodResponse> getCompletedMyPodMembers(@PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        PodResponse completedMembers = myPageService.getCompletedMembers(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, completedMembers);
    }

    //진행중인 참여 팟 리스트 조회
    @GetMapping("/inprogress/joinedpods")
    public BaseResponse<List<PodResponse>> getInprogressJoinedPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> inprogressJoinedPods = myPageService.getInprogressJoinedPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, inprogressJoinedPods);
    }

    //진행중인 참여 팟 정보 조회
    @GetMapping("/inprogress/joinedpods/{podId}")
    public BaseResponse<PodResponse> getInprogressJoinedPodInfo(@PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        PodResponse inprogressJoinedPodInfo = myPageService.getInprogressJoinedPodInfo(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, inprogressJoinedPodInfo);
    }

    //참여 팟에서 나의 주문서 상세 보기
    @GetMapping("/joinedpods/{podId}/order")
    public BaseResponse<OrderFormResponseDto.OrderFormDetailDto> getMyOrderFrom(@PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        OrderFormResponseDto.OrderFormDetailDto myOrderFrom = myPageService.getMyOrderFrom(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, myOrderFrom);
    }

    //완료된 참여 팟 리스트 조회
    @GetMapping("/completed/joinedpods")
    public BaseResponse<List<PodResponse>> getCompletedJoinedPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> completedJoinedPods = myPageService.getCompletedJoinedPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, completedJoinedPods);
    }

    //완료된 참여 팟 정보 조회
    @GetMapping("/completed/joinedpods/{podId}")
    public BaseResponse<PodResponse> getCompletedJoinedPodInfo(@PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        PodResponse completedJoinedPodInfo = myPageService.getCompletedJoinedPodInfo(podId, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, completedJoinedPodInfo);
    }

    //내가 남긴 후기 조회
    @GetMapping("/reviews/me")
    public BaseResponse<List<ReviewResponseDto.MyReview>> getMyReviews(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<ReviewResponseDto.MyReview> myReviews = myPageService.getMyReviews(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, myReviews);
    }

    //내가 받은 거래 후기 조회
    @GetMapping("/reviews/received")
    public BaseResponse<ReviewResponseDto.AboutMeReview> getReceivedReviews(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        ReviewResponseDto.AboutMeReview receivedReviews = myPageService.getReceivedReviews(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, receivedReviews);
    }
}
