package com.podmate.domain.crawling;

import org.springframework.stereotype.Service;

@Service
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public String fetchItemName(String url) {
        return "샘플 상품명 - 크롤링 적용 전";
    }

    @Override
    public int fetchPrice(String url) {
        return 15000;
    }

}
