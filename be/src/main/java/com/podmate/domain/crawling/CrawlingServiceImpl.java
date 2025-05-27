package com.podmate.domain.crawling;


import com.podmate.domain.platformInfo.domain.entity.PlatformInfo;
import com.podmate.domain.platformInfo.exception.PlatformNotSupportedException;
import com.podmate.domain.pod.domain.enums.Platform;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
    private final List<PlatformCrawler> crawlerList;

    private PlatformCrawler resolveCrawler(String platformName){
        return crawlerList.stream()
                .filter(c -> c.supports(platformName))
                .findFirst()
                .orElseThrow(() -> new PlatformNotSupportedException());
    }

    @Override
    public String fetchItemName(PlatformInfo platformInfo, String url) {
        return resolveCrawler(platformInfo.getPlatformName()).fetchItemName(url);
    }

    @Override
    public int fetchPrice(PlatformInfo platformInfo, String url) {
        return resolveCrawler(platformInfo.getPlatformName()).fetchPrice(url);
    }

}
