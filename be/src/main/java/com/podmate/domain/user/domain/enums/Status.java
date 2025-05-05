package com.podmate.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ACTIVE("ACTIVE", "현재 활성화된 사용자입니다."),
    INACTIVE("INACTIVE", "현재 비활성화된 사용자입니다."),
    BANNED("BANNED", "관리자에 의해 정지된 사용자입니다.");

    private final String name;
    private final String description;
}
