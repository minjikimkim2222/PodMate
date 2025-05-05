package com.podmate.domain.crawling;

// TODO : 크롤링 안 다룸. 샘플 데이터만 넘김
public interface CrawlingService {
    public String fetchItemName(String url);
    public int fetchPrice(String url);
}
