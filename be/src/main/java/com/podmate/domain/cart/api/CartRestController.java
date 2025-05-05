package com.podmate.domain.cart.api;

import com.podmate.domain.cart.application.CartService;
import com.podmate.domain.cart.dto.CartRequestDto;
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
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartRestController {
    private final CartService cartService;

    @PostMapping
    public BaseResponse<String> createCart(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody CartRequestDto.CartCreateRequest request
            ){
        String response = cartService.createCart(user.getUserId(), request);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }
}
