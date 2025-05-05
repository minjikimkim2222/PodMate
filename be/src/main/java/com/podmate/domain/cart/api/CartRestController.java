package com.podmate.domain.cart.api;

import com.podmate.domain.cart.application.CartService;
import com.podmate.domain.cart.dto.CartRequestDto;
import com.podmate.domain.cart.dto.CartResponseDto;
import com.podmate.domain.cart.dto.CartResponseDto.PlatformList;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public BaseResponse<CartResponseDto.PlatformList> getCartList(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(name = "podId", required = true) Long podId // 필수값 -- podId를 계속 전달해주기 위함
    ) {
        CartResponseDto.PlatformList response = cartService.getCartList(user.getUserId());

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    @PostMapping("/cartItem")
    public BaseResponse<String> addCartItems(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody CartRequestDto.CartItemRequest request
    ){
        String response = cartService.addCartItems(user.getUserId(), request);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    @DeleteMapping("/cartItems/{itemId}")
    public BaseResponse<String> deleteCartItem(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long itemId
    ){
        String response = cartService.deleteCartItem(user.getUserId(), itemId);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }
}
