package com.podmate.domain.review.domain.enums;

// TODO :: 샘플 리뷰만 작성했다. 값 수정 맘대로

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionText {
    // "텍스트형", "점수"
    // 5점
    EXACT_TRANSACTION("거래가 정확해요", 5),
    KIND("친절해요", 5),
    ON_TIME("시간 약속을 잘 지켜요", 5),
    QUICK_RESPONSE("응답이 빨라요", 5),

    // 4점

    // 3점

    // 2점

    // 1점
    BAD("별로예요", 1);

    private final String reviewText; // 프론트에서 받은 문구
    private final int reviewScore; // 점수

}
