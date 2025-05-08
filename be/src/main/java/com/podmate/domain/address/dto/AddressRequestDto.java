package com.podmate.domain.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AddressRequestDto {

    @Getter
    public static class AddressUpdateRequest {
        private String roadAddress;
        private Double latitude;
        private Double longitude;
    }

}
