package com.podmate.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewRequestDto {

    private Long podId;
    private List<String> options; // optionText 문자열 리스트
}
