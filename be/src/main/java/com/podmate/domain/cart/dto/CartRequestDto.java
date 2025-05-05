package com.podmate.domain.cart.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;

public class CartRequestDto {
    @Getter
    public static class CartCreateRequest {
        @NotBlank
        private String platformName;
    }

    @Getter
    public static class CartItemRequest {
        private Long platformInfoId;
        private List<Item> itemList;

        @Getter
        public static class Item {
            private int quantity;
            private String itemUrl;
            private String optionText;
        }
    }

}
