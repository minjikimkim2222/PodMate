package com.podmate.domain.pod.api;

import com.podmate.domain.address.dto.AddressRequestDto;
import com.podmate.domain.pod.application.PodService;
import com.podmate.domain.pod.domain.enums.SortBy;
import com.podmate.domain.pod.dto.PodRequestDto;
import com.podmate.domain.pod.dto.PodResponse;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pods")
public class PodRestController {

    private final PodService podService;

    //팟 리스트 조회
    @GetMapping
    public BaseResponse<List<PodResponse>> getPodList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                      @RequestParam(value = "sortBy", required = false) SortBy sortBy,
                                                      @RequestParam(value = "platform", required = false) List<String> platforms,
                                                      @RequestParam(value = "podType", required = false) String podType) {
        List<PodResponse> podList = podService.getPodList(customOAuth2User.getUserId(), sortBy, platforms, podType);
        return BaseResponse.onSuccess(SuccessStatus._OK, podList);
    }

    //팟 상세보기 조회
    @GetMapping("/{podId}")
    public BaseResponse<PodResponse> getPodDetailList(@PathVariable Long podId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getUserId();
        PodResponse response = podService.getPodDetail(userId, podId);
        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    //최소주문 팟 생성하기
    @PostMapping("/minimum-order")
    public BaseResponse<Long> createMinimumPod(@RequestBody PodRequestDto.MininumRequestDto request, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        Long podId = podService.createMinimum(request, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, podId);
    }

    //공동주문 팟 생성하기
    @PostMapping("/group-buy")
    public BaseResponse<Long> createGroupBuyPod(@RequestBody PodRequestDto.GroupBuyRequestDto request, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){

        Long podId = podService.createGroupBuy(request, customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, podId);
    }

    // 팟 생성 주소 저장하기
    @PostMapping("/addresses")
    public BaseResponse<Long> createAddress(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody AddressRequestDto.AddressUpdateRequest podAddressCreateRequest
            ){
        Long addressId = podService.createAddress(podAddressCreateRequest);

        return BaseResponse.onSuccess(SuccessStatus._CREATED, addressId);
    }


}
