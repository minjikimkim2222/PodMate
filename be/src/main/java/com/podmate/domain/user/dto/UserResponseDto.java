package com.podmate.domain.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInfo{
        private Long userId;
        private String role;
    }


    @Getter
    @AllArgsConstructor
    public static class AddressInfo {
        private Long userId;
        private Long addressId;
    }

    @Getter
    @AllArgsConstructor
    public static class AccountInfo {
        private Long userId;
        private String account;
    }

    @Getter
    @AllArgsConstructor
    public static class OtherUserProfileInfo {
        private Profile profile;
        private List<Review> reviews;

        @Getter
        @AllArgsConstructor
        public static class Profile {
            private String nickname;
            private String profileImageUrl;
            private Double mannerScore;
        }

        @Getter
        @AllArgsConstructor
        public static class Review {
            private Long reviewId;
            private List<String> reviewOptions;
            private int reviewCount;
        }
    }
}
