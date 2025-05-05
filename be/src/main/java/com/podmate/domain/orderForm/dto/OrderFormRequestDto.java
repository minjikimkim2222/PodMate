package com.podmate.domain.orderForm.dto;

import java.util.List;
import lombok.Getter;


public class OrderFormRequestDto {
    @Getter
    public static class ItemIds {
        private List<Long> items;
    }


}
