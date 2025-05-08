package com.podmate.domain.orderForm.converter;

import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.orderForm.domain.entity.OrderItem;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto.OrderFormDetailDto.ItemDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto.OrderFormListResponseDto;
import com.podmate.domain.orderForm.dto.OrderFormResponseDto.OrderFormListResponseDto.OrderFormDto;
import java.util.List;
import java.util.stream.Collectors;

public class OrderFormConverter {
    public static OrderFormResponseDto.OrderFormListResponseDto toListResponseDto(List<OrderForm> orderForms){
        List<OrderFormResponseDto.OrderFormListResponseDto.OrderFormDto> orderFormDtos = orderForms.stream()
                .map(orderForm -> new OrderFormDto(
                        orderForm.getId(),
                        orderForm.getPlatform(),
                        orderForm.getTotalAmount()
                ))
                .collect(Collectors.toList());

        return new OrderFormResponseDto.OrderFormListResponseDto(orderFormDtos);

    }

    public static OrderFormResponseDto.OrderFormDetailDto toDetailResponseDto(List<OrderItem> orderItems, int totalAmount){
        List<OrderFormResponseDto.OrderFormDetailDto.ItemDto> itemDtos = orderItems.stream()
                .map(item -> new ItemDto(
                        item.getItemName(),
                        item.getQuantity(),
                        item.getOptionText(),
                        item.getItemUrl(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderFormResponseDto.OrderFormDetailDto(itemDtos, totalAmount);
    }

}
