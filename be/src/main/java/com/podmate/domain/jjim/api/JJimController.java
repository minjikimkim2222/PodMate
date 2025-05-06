package com.podmate.domain.jjim.api;

import com.podmate.domain.jjim.application.JJimService;
import com.podmate.domain.jjim.dto.JJimRequestDto;
import com.podmate.domain.jjim.dto.JJimResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public BaseResponse<List<JJimResponseDto>> getJJimList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        List<JJimResponseDto> jjimList = jjimService.getJJimList(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, jjimList);
    }
}
