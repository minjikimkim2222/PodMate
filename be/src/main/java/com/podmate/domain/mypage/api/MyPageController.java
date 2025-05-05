package com.podmate.domain.mypage.api;

import com.podmate.domain.mypage.application.MyPageService;
import com.podmate.domain.mypage.dto.MyPageRequestDto;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
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

    //운송장 입력
    @PostMapping("/inprogress/mypods/{podId}")
    public BaseResponse<String> addTrackingNumber(@RequestBody MyPageRequestDto request, @PathVariable("podId") Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
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

    //완료된 참여 팟 리스트 조회
    @GetMapping("/completed/joinedpods")
    public BaseResponse<List<PodResponse>> getCompletedJoinedPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> completedJoinedPods = myPageService.getCompletedJoinedPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, completedJoinedPods);
    }
}
