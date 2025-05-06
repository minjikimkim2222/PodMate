package com.podmate.domain.jjim.api;

import com.podmate.domain.jjim.application.JJimService;
import com.podmate.domain.jjim.dto.JJimRequestDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jjims")
public class JJimController {

    private final JJimService jjimService;

    @PostMapping
    public BaseResponse<String> createJJim(@RequestBody JJimRequestDto requestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        jjimService.createJJim(requestDto.getPodId(), customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._CREATED, "successfully saved jjim");
    }
}
