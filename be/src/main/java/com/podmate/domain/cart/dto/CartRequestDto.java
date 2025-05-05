package com.podmate.domain.cart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CartRequestDto {
    @Getter
    public static class CartCreateRequest {
        @NotBlank
        private String platformName;
    }

}
