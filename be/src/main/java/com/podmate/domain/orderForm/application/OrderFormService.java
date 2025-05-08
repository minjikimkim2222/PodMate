package com.podmate.domain.orderForm.application;

import com.podmate.domain.orderForm.dto.OrderFormResponseDto;
import java.util.List;

public interface OrderFormService {
    Long createOrderForm(Long userId, Long podId, List<Long> itemIds);
    OrderFormResponseDto.OrderFormListResponseDto getMyOrderForms(Long userId);

    OrderFormResponseDto.OrderFormDetailDto getMyOrderFormDetail(Long userId, Long orderformId);
}
