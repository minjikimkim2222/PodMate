package com.podmate.domain.mypage.api;

import com.podmate.domain.mypage.application.MyPageService;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/inprogress/mypods")
    public BaseResponse<List<PodResponse>> getInprogressMyPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        List<PodResponse> inprogressMyPods = myPageService.getInprogressMyPods(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, inprogressMyPods);
    }
}
