package com.podmate.domain.review.dto;

import com.podmate.domain.pod.dto.PodResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class ReviewResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReviewTarget{
        private Long podId;
        private String podType;
        private List<PodMember> podMembers;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class PodMember{
        private Long userId;
        private String nickname;
        private String profileImageUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MyReview{
        private PodMember recipient;
        private List<String> optionTexts;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AboutMeReview{
        private Profile profile;
        private List<ReceivedReview> reviews;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Profile{
        private String nickname;
        private String profileImageUrl;
        private double mannerScore;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReceivedReview{
        private String optionText;
        private int amount;     //optionText에 대하여 받은 개수
    }

}
