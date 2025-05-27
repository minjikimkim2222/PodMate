package com.podmate.domain.crawling;

import com.podmate.domain.pod.domain.enums.Platform;
import org.springframework.stereotype.Component;

@Component("ALIEXPRESS")
public class AliExpressCrawler implements PlatformCrawler{

    @Override
    public boolean supports(String platformName) {
        return "ALIEXPRESS".equalsIgnoreCase(platformName);
    }

    @Override
    public String fetchItemName(String itemUrl) {
        return null;
    }

    @Override
    public int fetchPrice(String itemUrl) {
        return 0;
    }

}
