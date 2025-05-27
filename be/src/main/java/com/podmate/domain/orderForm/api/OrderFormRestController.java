package com.podmate.domain.orderForm.api;

import com.podmate.domain.notification.application.NotificationService;
import com.podmate.domain.orderForm.application.OrderFormService;
import com.podmate.domain.orderForm.dto.OrderFormRequestDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto.OrderFormDetailDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto.OrderFormListResponseDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderforms")
@RequiredArgsConstructor
public class OrderFormRestController {
    private final OrderFormService orderFormService;
    private final NotificationService notificationService;

    @PostMapping
    public BaseResponse<Long> createOrderForm(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(name = "podId", required = true) Long podId,
            @RequestBody OrderFormRequestDto.ItemIds request
            ){
        Long orderFormId = orderFormService.createOrderForm(user.getUserId(), podId, request.getItems());

        //알림 전송
        notificationService.notifyParticipationRequest(user.getUserId(), podId);

        return BaseResponse.onSuccess(SuccessStatus._OK, orderFormId);
    }

    @GetMapping
    public BaseResponse<OrderFormResponseDto.OrderFormListResponseDto> getMyOrderForms(
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        OrderFormResponseDto.OrderFormListResponseDto response = orderFormService.getMyOrderForms(user.getUserId());

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

    @GetMapping("/{orderformId}")
    public BaseResponse<OrderFormResponseDto.OrderFormDetailDto> getMyOrderFormDetail(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long orderformId
    ){
        OrderFormResponseDto.OrderFormDetailDto response = orderFormService.getMyOrderFormDetail(
                user.getUserId(),
                orderformId);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }

}
