package com.podmate.domain.crawling;

import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;

// TODO : 크롤링 안 다룸. 샘플 데이터만 넘김
public interface CrawlingService {
    public String fetchItemName(PlatformInfo platformInfo, String url);
    public int fetchPrice(PlatformInfo platformInfo, String url);
}
