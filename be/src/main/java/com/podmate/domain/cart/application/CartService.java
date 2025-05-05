package com.podmate.domain.cart.application;

import com.podmate.domain.cart.dto.CartRequestDto;
import com.podmate.domain.cart.dto.CartResponseDto;

public interface CartService {
    String createCart(Long userId, CartRequestDto.CartCreateRequest request);
    CartResponseDto.PlatformList getCartList(Long userId);

    String addCartItems(Long userId, CartRequestDto.CartItemRequest request);

}
