package com.podmate.domain.crawling;

import com.podmate.domain.pod.domain.enums.Platform;

public interface PlatformCrawler {
    boolean supports(String platformName); // 어떤 플랫폼을 처리할 수 있는지
    String fetchItemName(String itemUrl);
    int fetchPrice(String itemUrl);

}
