package com.podmate.domain.user.api;

import com.podmate.domain.address.dto.AddressRequestDto;
import com.podmate.domain.user.application.UserService;
import com.podmate.domain.user.dto.UserResponseDto;
import com.podmate.domain.user.dto.UserResponseDto.AddressInfo;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    // 로그인된 유저 정보를 확인하는 api
    private final UserService userService;
    @GetMapping("/me")
    public BaseResponse<UserResponseDto.MyInfo> getMyInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        UserResponseDto.MyInfo response = userService.getMyInfo(customOAuth2User.getUser());

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    @PutMapping("/me/address")
    public BaseResponse<UserResponseDto.AddressInfo> updateAddress(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody AddressRequestDto.UserAddressUpdateRequest userAddressUpdateRequest
            ){
        UserResponseDto.AddressInfo response = userService.updateAddress(customOAuth2User.getUserId(), userAddressUpdateRequest);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

}
