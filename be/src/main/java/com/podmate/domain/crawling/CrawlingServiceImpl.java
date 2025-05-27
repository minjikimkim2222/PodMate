package com.podmate.domain.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrawlingServiceImpl implements CrawlingService {

    private WebDriver createStealthChromeDriver() {
        WebDriverManager.chromedriver().setup(); // WebDriverManager가 시스템에 설치된 Chrome 버전(예: 137)에 맞는 드라이버를 가져옴
        ChromeOptions options = new ChromeOptions();

        // options.addArguments("--headless"); // UI 없이 백그라운드 실행 (배포시에 주석 처리)

        // 크롤링 봇 방지
//        // 1. User-Agent 설정 (필수 - 봇 탐지 우회의 기본)
//        // options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"); -- 윈도우 환경
//        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");  // -- mac 환경
//
//        // 2. 자동화 제어 플래그 비활성화 (많은 사이트가 이 플래그로 봇 감지)
//        options.addArguments("--disable-blink-features=AutomationControlled");
//        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation")); // Java에서는 List.of() 사용 가능
//        options.setExperimentalOption("useAutomationExtension", false);
//
//        // 3. GPU 가속 비활성화 (특히, headless 모드 또는 일부 불안정한 환경에서 안정성 향상)
//        options.addArguments("--disable-gpu");
//
//        // 4. 시크릿 모드 (쿠키/캐시의 영향을 최소화하여, 일관된 테스트/크롤링 환경 제공)
//        options.addArguments("--incognito");
//
//        // 5. 팝업 및 알림 차단
//        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--disable-notifications");
//
//        // 6. CORS 및 로컬 파일 접근 관련
//        options.addArguments("--remote-allow-origins=*");


        log.info("적용된 ChromeOptions: {}", options.asMap());
        try {
            return new ChromeDriver(options);
        } catch (Exception e) {
            log.error("ChromeDriver 생성 중 예외 발생: ", e);
            throw e;
        }
    }

    @Override
    public String fetchItemName(String itemUrl) {
       // WebDriver driver = new ChromeDriver();
        WebDriver driver = createStealthChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            driver.get(itemUrl); // 사용자로부터 받은 itemUrl로 접속
            log.info("접속한 URL :: {}", itemUrl);
            log.info("페이지 제목 :: {}", driver.getTitle());    // 페이지 제목 확인

            try {
                Alert alert = driver.switchTo().alert();
                log.warn("alert창 감지됨: {}", alert.getText());
                alert.accept();
                throw new RuntimeException("alert 발생으로 크롤링 중단: " + alert.getText());
            } catch (NoAlertPresentException ignored) {}

            // 스크롤 (필요하다면 유지)
            //((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

            // 또는 스크롤 후 특정 요소가 로드될 때까지 기다리는 다른 WebDriverWait를 추가할 수도 있습니다.

            log.info("페이지 스크롤 완료. 현재 URL: {}", driver.getCurrentUrl());

            WebElement productNameElement = wait.until(
                  //  ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.product-title span.twc-font-bold"))
                  //  ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.prod-buy-header__title"))
                   // -- 네이버 쇼핑
                   // ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#content > div._2-I30XS1lA > div._2QCa6wHHPy > fieldset > div._3k440DUKzy > div._1eddO7u4UC > h3")) -- 이거 너무 구체적
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[class*='_copyable']"))
                    //  ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3._22kNQuEXmb _copyable")) -- 이거 안됨
            );

            log.info("productNameElement :: {}, getText :: {}", productNameElement, productNameElement.getText());
            return productNameElement.getText();
        } catch (Exception e) {
            log.error("상품명 요소를 찾는 데 시간 초과. URL: {}", itemUrl);
            // 현재 페이지 소스를 출력하여 어떤 페이지가 로드되었는지 확인
            String pageSource = driver.getPageSource();
         //   log.error("페이지 소스 (전체): {}", pageSource); // 전체 긁어온 페이지 -- 디버깅

            throw new RuntimeException("크롤링 서비스 중, 상품명을 가져오는도중 에러 발생 : " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @Override
    public int fetchPrice(String itemUrl) {
        WebDriver driver = createStealthChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get(itemUrl); // 사용자로부터 받은 itemUrl로 접속

            try {
                Alert alert = driver.switchTo().alert();
                log.warn("alert창 감지됨: {}", alert.getText());
                alert.accept();
                throw new RuntimeException("alert 발생으로 크롤링 중단: " + alert.getText());
            } catch (NoAlertPresentException ignored) {}

            String productInfoAreaSelector = "div._3k440DUKzy"; // 상품 상세정보 주요 영역부터 찾기 (영역 좁히기)

            WebElement productInfoArea = wait.until(
               //     ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span._1LY7DqCnwR"))
                  //  ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#content > div._2-I30XS1lA > div._2QCa6wHHPy > fieldset > div._3k440DUKzy > div.WrkQhIlUY0 > div > strong > span._1LY7DqCnwR"))
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(productInfoAreaSelector))
            );

            // 찾은 상품상세정보 내에서 가격요소 찾기
            WebElement productPriceElement = productInfoArea.findElement(
                    By.cssSelector("strong[class*='aICRqgP9zw'] span:not(.blind):not(.won)"));

            log.info("productNameElement::{}, getText :: {}", productPriceElement, productPriceElement.getText());

            String priceText = productPriceElement.getText();
            // 가격 텍스트에서 숫자만 추출 (예: "7,900" -> 7900)
            String priceDigits = priceText.replaceAll("[^0-9]", "");
            if (priceDigits.isEmpty()) {
                throw new RuntimeException("가격 텍스트에서 숫자를 추출할 수 없습니다: " + priceText);
            }
            return Integer.parseInt(priceDigits);
        } catch (Exception e) {
            log.error("상품가격 요소를 찾는 데 시간 초과. URL: {}", itemUrl);
            // 현재 페이지 소스를 출력하여 어떤 페이지가 로드되었는지 확인
            String pageSource = driver.getPageSource();
            log.error("페이지 소스 (전체): {}", pageSource); // 전체 긁어온 페이지 -- 디버깅

            throw new RuntimeException("크롤링 서비스 중, 상품명을 가져오는도중 에러 발생 : " + e.getMessage());
        } finally {
            driver.quit();
        }

    }

}
