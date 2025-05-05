package com.podmate.domain.cart.application;

import com.podmate.domain.cart.dto.CartRequestDto;

public interface CartService {
    String createCart(Long userId, CartRequestDto.CartCreateRequest request);

}
