package com.podmate.domain.pod.domain.enums;

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
}
