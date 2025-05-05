package com.podmate.domain.pod.domain.enums;

import com.podmate.domain.platformInfo.exception.PlatformNotSupportedException;
import java.util.Arrays;

public enum Platform {

    COUPANG,
    UNKNOWN;
    //아직 크롤링 할 플랫폼 안 정함

    public static Platform fromDisplayName(String platform) {
        if(platform.equals("쿠팡")){
            return COUPANG;
        }else{
            return UNKNOWN;
        }
    }

    public static Platform validatePlatform(String platformName){
        return Arrays.stream(Platform.values())
                .filter(p -> p.name().equalsIgnoreCase(platformName))
                .findFirst()
                .orElseThrow(() -> new PlatformNotSupportedException());
    }
}
