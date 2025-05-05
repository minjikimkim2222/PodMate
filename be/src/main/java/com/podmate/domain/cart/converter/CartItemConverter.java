package com.podmate.domain.cart.converter;

import com.podmate.domain.cart.domain.entity.CartItem;
import com.podmate.domain.cart.dto.CartResponseDto;
import com.podmate.domain.cart.dto.CartResponseDto.CartItemList;
import com.podmate.domain.cart.dto.CartResponseDto.CartItemList.CartItemDto;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemConverter {
    public static CartResponseDto.CartItemList toCartItemListResponse(List<CartItem> cartItems){
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(item -> CartItemDto.builder()
                        .itemId(item.getId())
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .optionText(item.getOptionText())
                        .itemUrl(item.getItemUrl())
                        .build()
                )
                .collect(Collectors.toList());

        return new CartResponseDto.CartItemList(cartItemDtos);

    }
}
