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
        private Long userID;
        private String nickname;
        private String profileImageUrl;
    }
}
