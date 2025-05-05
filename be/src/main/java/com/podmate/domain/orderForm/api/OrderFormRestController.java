package com.podmate.domain.orderForm.api;

import com.podmate.domain.orderForm.application.OrderFormService;
import com.podmate.domain.orderForm.dto.OrderFormRequestDto;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    public BaseResponse<Long> createOrderForm(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(name = "podId", required = true) Long podId,
            @RequestBody OrderFormRequestDto.ItemIds request
            ){
        Long orderFormId = orderFormService.createOrderForm(user.getUserId(), podId, request.getItems());

        return BaseResponse.onSuccess(SuccessStatus._OK, orderFormId);
    }

}
