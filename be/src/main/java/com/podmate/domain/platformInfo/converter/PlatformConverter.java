package com.podmate.domain.platformInfo.converter;

import com.podmate.domain.cart.dto.CartResponseDto;
import com.podmate.domain.cart.dto.CartResponseDto.PlatformDto;
import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformConverter {
    public static CartResponseDto.PlatformList toPlatformList(List<PlatformInfo> platformInfos){
        List<PlatformDto> platformDtos = platformInfos.stream()
                .map(info -> new PlatformDto(info.getId(), info.getPlatformName()))
                .collect(Collectors.toList());

        return new CartResponseDto.PlatformList(platformDtos);
    }
}
