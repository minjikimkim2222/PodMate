package com.podmate.domain.jjim.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JJimResponseDto {

    private Long podId;
    private String podName;
    private String podType;
    private String platform;
    private int goalAmount;
    private int currentAmount;
}
