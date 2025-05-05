package com.podmate.domain.orderForm.application;

import java.util.List;

public interface OrderFormService {
    Long createOrderForm(Long userId, Long podId, List<Long> itemIds);

}
