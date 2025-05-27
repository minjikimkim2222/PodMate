package com.podmate.domain.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom; // 랜덤 딜레이용
import org.openqa.selenium.JavascriptExecutor; // 스크롤링용

//@Component("COUPANG")
//@Slf4j
//public class CoupangCrawler implements PlatformCrawler {
//
//    // 4.1 User-Agent Rotation
//    private static final List<String> USER_AGENTS = Arrays.asList(
//            // 모바일 에이전트
//            "Mozilla/5.0 (iPhone; CPU iPhone OS 17_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1",
//            "Mozilla/5.0 (Linux; Android 13; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36",
//            // 데스크톱 에이전트 (기존 사용하시던 것도 좋고, 더 최신 버전으로 업데이트 가능)
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
//            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
//    );
//    private static final Random RANDOM = new Random();
//
//    @Override
//    public boolean supports(String platformName) {
//        return "COUPANG".equalsIgnoreCase(platformName);
//    }
//
//    private WebDriver createStealthChromeDriver() {
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//
//        // --- 안티봇 및 최적화 옵션 ---
//        // 랜덤 User-Agent 선택
//        String randomUserAgent = USER_AGENTS.get(RANDOM.nextInt(USER_AGENTS.size()));
//        options.addArguments("user-agent=" + randomUserAgent);
//        log.info("Selected User-Agent: {}", randomUserAgent);
//
//        // 헤드리스 모드 설정 (문서의 "--headless=new" 스타일 적용 시도)
//        // options.addArguments("--headless=new"); // 최신 Chrome의 네이티브 헤드리스. headless만 써도 무방.
//        // options.addArguments("--headless"); // 기존 방식, UI 없이 백그라운드 실행
//        // 디버깅 시에는 위 두 줄을 주석 처리하고, 아래 창 크기 옵션을 활성화
//        options.addArguments("window-size=1920x1080"); // 헤드리스가 아닐 때 창 크기 지정
//
//
//        // 자동화 제어 플래그 비활성화 (봇 탐지 우회)
//        options.addArguments("--disable-blink-features=AutomationControlled");
//        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//        options.setExperimentalOption("useAutomationExtension", false);
//
//        // GPU 가속 비활성화 (안정성 향상)
//        options.addArguments("--disable-gpu");
//
//        // 시크릿 모드 (쿠키/캐시 영향 최소화)
//        options.addArguments("--incognito");
//
//        // 팝업 및 알림 차단
//        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--disable-notifications");
//
//        // 기타 안정성/호환성 옵션
//        options.addArguments("--remote-allow-origins=*"); // CORS
//        options.addArguments("--no-sandbox"); // Linux/Docker 환경에서 필요
//        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 문제 방지
//        options.addArguments("--lang=ko-KR"); // 한국어 설정
//
//        // 불필요한 리소스 로딩 줄이기 (선택적, 성능 향상에 도움될 수 있으나 일부 사이트 문제 유발 가능)
//        // options.addArguments("--blink-settings=imagesEnabled=false"); // 이미지 로딩 안함
//        // options.setPageLoadStrategy(PageLoadStrategy.EAGER); // DOM 로드 후 대기, 리소스는 계속
//
//        log.info("적용된 ChromeOptions (Coupang): {}", options.asMap());
//        try {
//            return new ChromeDriver(options);
//        } catch (Exception e) {
//            log.error("ChromeDriver (Coupang) 생성 중 예외 발생: ", e);
//            throw new RuntimeException("ChromeDriver (Coupang) 생성 실패", e); // 명시적인 예외 throw
//        }
//    }
//
//    /**
//     * 페이지를 인간처럼 스크롤합니다. (문서 2.2 BaseScraper의 scroll_page 참고)
//     * @param driver WebDriver 인스턴스
//     * @param scrollPause 스크롤 간 대기 시간 (초 단위)
//     */
//    private void scrollPageLikeHuman(WebDriver driver, double scrollPause) {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");
//
//        log.info("인간적인 스크롤 시작. 초기 높이: {}", lastHeight);
//
//        while (true) {
//            // 랜덤한 스크롤 양
//            int scrollAmount = ThreadLocalRandom.current().nextInt(300, 701);
//            js.executeScript("window.scrollBy(0, " + scrollAmount + ");");
//            log.debug("Scrolled by: {}px", scrollAmount);
//
//            // 자연스러운 대기 시간 (밀리초 단위)
//            try {
//                Thread.sleep(ThreadLocalRandom.current().nextInt((int)(scrollPause * 800), (int)(scrollPause * 1200) + 1));
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.warn("스크롤 중 대기 시간 인터럽트됨", e);
//                break;
//            }
//
//            long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
//            if (newHeight == lastHeight) {
//                log.info("스크롤 완료. 페이지 하단 도달. 최종 높이: {}", newHeight);
//                break;
//            }
//            lastHeight = newHeight;
//            log.debug("새로운 페이지 높이: {}", newHeight);
//        }
//    }
//
//    /**
//     * 랜덤한 시간 동안 대기합니다. (자연스러운 동작 구현)
//     * @param minMillis 최소 대기 시간 (밀리초)
//     * @param maxMillis 최대 대기 시간 (밀리초)
//     */
//    private void randomDelay(int minMillis, int maxMillis) {
//        try {
//            int delay = ThreadLocalRandom.current().nextInt(minMillis, maxMillis + 1);
//            log.debug("랜덤 딜레이 적용: {}ms", delay);
//            Thread.sleep(delay);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            log.warn("랜덤 딜레이 중 인터럽트 발생", e);
//        }
//    }
//
//    // 쿠팡의 "Not Found" 또는 "상품 없음" 페이지를 감지하는 로직 (필요시 구체화)
//    private boolean isPageNotFound(WebDriver driver, String itemUrl) {
//        // 예시: 특정 문구 또는 요소로 판단
//        try {
//            WebElement errorElement = driver.findElement(By.xpath("//*[contains(text(),'요청하신 페이지를 찾을 수 없습니다')] | //*[contains(text(),'판매가 종료된 상품입니다')] | //div[@class='exception-message-box']"));
//            if (errorElement.isDisplayed()) {
//                log.warn("페이지를 찾을 수 없음 (에러 메시지 감지). URL: {}", itemUrl);
//                return true;
//            }
//        } catch (NoSuchElementException e) {
//            // 해당 요소가 없으면 정상 페이지일 가능성
//        }
//        String title = driver.getTitle().toLowerCase();
//        if (title.contains("error") || title.contains("에러") || title.contains("찾을 수 없습니다") || title.contains("상품없음")) {
//            log.warn("페이지를 찾을 수 없음 (페이지 제목으로 판단). 제목: {}, URL: {}", driver.getTitle(), itemUrl);
//            return true;
//        }
//        return false;
//    }
//
//
//    @Override
//    public String fetchItemName(String itemUrl) {
//        WebDriver driver = createStealthChromeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // 대기 시간 약간 늘림
//
//        try {
//            driver.get(itemUrl);
//            log.info("쿠팡 상품명 크롤링 - 접속 URL: {}", itemUrl);
//            randomDelay(1500, 3000); // 페이지 로드 후 자연스러운 대기
//
//            // 페이지가 정상적으로 로드되었는지 확인
//            if (isPageNotFound(driver, itemUrl)) {
//                throw new RuntimeException("상품 페이지를 찾을 수 없습니다 (Not Found). URL: " + itemUrl);
//            }
//
//            // 쿠팡 상품 상세 페이지는 스크롤이 크게 필요 없을 수 있지만,
//            // 필요하다면 아래 주석 해제 또는 간단한 스크롤 추가
//            // scrollPageLikeHuman(driver, 1.0); // 1초 간격으로 스크롤
//
//            WebElement productNameElement = wait.until(
//                    ExpectedConditions.visibilityOfElementLocated(
//                            By.cssSelector("h1[data-sentry-component='ProductTitle'] span.twc-font-bold"))
//            );
//
//            String itemName = productNameElement.getText().trim();
//            if (itemName.isEmpty()){
//                // 만약 span 안의 텍스트가 비었다면, h1 전체 텍스트 시도
//                WebElement h1Element = driver.findElement(By.cssSelector("h1[data-sentry-component='ProductTitle'] span.twc-font-bold"));
//                itemName = h1Element.getText().trim();
//                log.warn("span.twc-font-bold가 비어있어 h1 전체 텍스트 사용: {}", itemName);
//            }
//
//            log.info("추출된 상품명: {}", itemName);
//            return itemName;
//
//        } catch (TimeoutException e) {
//            log.error("상품명 요소를 찾는 데 시간 초과. URL: {}. Error: {}", itemUrl, e.getMessage());
//            capturePageSourceAndScreenshot(driver, "fetchItemName_Timeout");
//            throw new RuntimeException("크롤링 서비스 중, 상품명을 가져오는 데 시간 초과 발생: " + itemUrl, e);
//        } catch (NoSuchElementException e) {
//            log.error("상품명 요소를 찾을 수 없음. CSS 선택자를 확인하세요. URL: {}. Error: {}", itemUrl, e.getMessage());
//            capturePageSourceAndScreenshot(driver, "fetchItemName_NoSuchElement");
//            throw new RuntimeException("크롤링 서비스 중, 상품명 요소를 찾을 수 없음: " + itemUrl, e);
//        } catch (Exception e) {
//            log.error("상품명 가져오기 중 예상치 못한 에러 발생. URL: {}. Error: {}", itemUrl, e.getMessage(), e);
//            capturePageSourceAndScreenshot(driver, "fetchItemName_Exception");
//            throw new RuntimeException("크롤링 서비스 중, 상품명 가져오기 에러 발생: " + itemUrl, e);
//        } finally {
//            if (driver != null) {
//                driver.quit();
//            }
//        }
//    }
//
//    @Override
//    public int fetchPrice(String itemUrl) {
//        WebDriver driver = createStealthChromeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//
//        try {
//            driver.get(itemUrl);
//            log.info("쿠팡 가격 크롤링 - 접속 URL: {}", itemUrl);
//            randomDelay(1500, 3000);
//
//            if (isPageNotFound(driver, itemUrl)) {
//                throw new RuntimeException("상품 페이지를 찾을 수 없습니다 (Not Found). URL: " + itemUrl);
//            }
//
//            // scrollPageLikeHuman(driver, 1.0); // 필요시
//
//            WebElement priceElement;
//            try {
//                // 우선 할인 가격이나 최종 판매가를 나타내는 요소를 찾습니다.
//                priceElement = wait.until(
//                        ExpectedConditions.visibilityOfElementLocated(
//                                By.cssSelector("div[data-sentry-component='SalesPrice'] .price-amount")
//                                // 위 선택자는 SalesPrice 컴포넌트의 price-amount 또는 일반적인 판매가 구조(total-price strong)를 포함합니다.
//                                // 실제 쿠팡 페이지 구조에 따라 더 정확한 선택자를 사용해야 할 수 있습니다.
//                                // 예: "span.total-price" , ".prod-sale-price .price-value" 등
//                        )
//                );
//            } catch (TimeoutException e) {
//                // '품절' 등 가격이 없는 경우 명시적으로 처리
//                try {
//                    WebElement soldOutElement = driver.findElement(By.xpath("//*[contains(text(),'일시 품절') or contains(text(),'판매 중단')]"));
//                    if (soldOutElement.isDisplayed()) {
//                        log.warn("상품이 품절 또는 판매 중단되었습니다. 가격: 0 처리. URL: {}", itemUrl);
//                        return 0; // 품절 시 0원 또는 특정 값으로 처리
//                    }
//                } catch (NoSuchElementException ignored) {}
//                log.error("상품 가격 요소를 찾는 데 시간 초과. URL: {}. Error: {}", itemUrl, e.getMessage());
//                capturePageSourceAndScreenshot(driver, "fetchPrice_Timeout");
//                throw new RuntimeException("크롤링 서비스 중, 상품 가격을 가져오는 데 시간 초과 발생: " + itemUrl, e);
//            }
//
//
//            String priceText = priceElement.getText();
//            log.info("추출된 가격 텍스트: {}", priceText);
//
//            String digits = priceText.replaceAll("[^0-9]", "");
//            if (digits.isEmpty()) {
//                // 가격이 없거나, "가격 문의" 등 다른 텍스트인 경우
//                log.warn("가격에서 숫자 추출 실패: '{}'. 가격 정보가 없거나 다른 형식일 수 있습니다. URL: {}", priceText, itemUrl);
//                // 이 경우 0을 반환하거나 예외를 던질 수 있습니다. 요구사항에 따라 결정.
//                return 0; // 예시로 0 반환
//            }
//
//            log.info("변환된 가격 (숫자): {}", digits);
//            return Integer.parseInt(digits);
//
//        } catch (NoSuchElementException e) {
//            log.error("상품 가격 요소를 찾을 수 없음. CSS 선택자를 확인하세요. URL: {}. Error: {}", itemUrl, e.getMessage());
//            capturePageSourceAndScreenshot(driver, "fetchPrice_NoSuchElement");
//            throw new RuntimeException("크롤링 서비스 중, 상품 가격 요소를 찾을 수 없음: " + itemUrl, e);
//        } catch (Exception e) {
//            log.error("상품 가격 가져오기 중 예상치 못한 에러 발생. URL: {}. Error: {}", itemUrl, e.getMessage(), e);
//            capturePageSourceAndScreenshot(driver, "fetchPrice_Exception");
//            throw new RuntimeException("크롤링 서비스 중, 상품 가격 가져오기 에러 발생: " + itemUrl, e);
//        } finally {
//            if (driver != null) {
//                driver.quit();
//            }
//        }
//    }
//
//    // 디버깅을 위한 페이지 소스 및 스크린샷 캡처 유틸리티 (필요시 사용)
//    private void capturePageSourceAndScreenshot(WebDriver driver, String filePrefix) {
//        try {
//            String pageSource = driver.getPageSource();
//            log.error("페이지 소스 ({}): {}", filePrefix, pageSource.substring(0, Math.min(pageSource.length(), 4000))); // 너무 길면 잘라서 로깅
//
//            // 스크린샷 캡처 (파일로 저장 - 실제 운영에서는 경로 등 고려 필요)
//            // File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            // Path destination = Paths.get(filePrefix + "_" + System.currentTimeMillis() + ".png");
//            // Files.copy(screenshot.toPath(), destination);
//            // log.info("스크린샷 저장됨: {}", destination);
//        } catch (Exception e) {
//            log.error("페이지 소스 또는 스크린샷 캡처 중 오류", e);
//        }
//    }
//}

// 아래코드는 알리,,

//@Component("COUPANG")
//@Slf4j
//public class CoupangCrawler implements PlatformCrawler {
//
//    @Override
//    public boolean supports(String platformName) {
//        return "COUPANG".equalsIgnoreCase(platformName);
//    }
//
//    private WebDriver createStealthChromeDriver() {
//        WebDriverManager.chromedriver().setup(); // WebDriverManager가 시스템에 설치된 Chrome 버전(예: 137)에 맞는 드라이버를 가져옴
//        ChromeOptions options = new ChromeOptions();
//
//        // options.addArguments("--headless"); // UI 없이 백그라운드 실행 (배포시에 주석 처리)
//
//        // 크롤링 봇 방지
//        // 1. User-Agent 설정 (필수 - 봇 탐지 우회의 기본)
//        // options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"); -- 윈도우 환경
//         options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");  // -- mac 환경
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
//         options.addArguments("--incognito");
//
//        // 5. 팝업 및 알림 차단
//        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--disable-notifications");
//
//        // 6. CORS 및 로컬 파일 접근 관련
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--no-sandbox"); // Linux/Docker 환경에서 필요할 수 있음
//        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 문제 방지
//        options.addArguments("--lang=ko-KR"); // 언어/지역 설정 (사이트 반응에 영향 줄 수 있음
//
//
//        log.info("적용된 ChromeOptions: {}", options.asMap());
//        try {
//            return new ChromeDriver(options);
//        } catch (Exception e) {
//            log.error("ChromeDriver 생성 중 예외 발생: ", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public String fetchItemName(String itemUrl) {
//
//        WebDriver driver = createStealthChromeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//
//        try {
//            driver.get(itemUrl); // 사용자로부터 받은 itemUrl로 접속
//            // log.info("접속한 URL :: {}", itemUrl);
//            // log.info("페이지 제목 :: {}", driver.getTitle());    // 페이지 제목 확인
//
//            try {
//                Alert alert = driver.switchTo().alert();
//                log.warn("alert창 감지됨: {}", alert.getText());
//                alert.accept();
//                throw new RuntimeException("alert 발생으로 크롤링 중단: " + alert.getText());
//            } catch (NoAlertPresentException ignored) {}
//
//
//            log.info("페이지 스크롤 완료. 현재 URL: {}", driver.getCurrentUrl());
//
//            WebElement productNameElement = wait.until(
//                    ExpectedConditions.visibilityOfElementLocated(
//                            By.cssSelector("h1[data-sentry-component='ProductTitle'] span.twc-font-bold"))
//
//            );
//
//            log.info("productNameElement :: {}, getText :: {}", productNameElement, productNameElement.getText());
//            return productNameElement.getText();
//        } catch (Exception e) {
//            log.error("상품명 요소를 찾는 데 시간 초과. URL: {}", itemUrl);
//            // 현재 페이지 소스를 출력하여 어떤 페이지가 로드되었는지 확인
//            String pageSource = driver.getPageSource();
//            log.error("페이지 소스 (전체): {}", pageSource); // 전체 긁어온 페이지 -- 디버깅
//
//            throw new RuntimeException("크롤링 서비스 중, 상품명을 가져오는도중 에러 발생 : " + e.getMessage());
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Override
//    public int fetchPrice(String itemUrl) {
//        WebDriver driver = createStealthChromeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        try {
//            driver.get(itemUrl); // 사용자로부터 받은 itemUrl로 접속
//
//            try {
//                Alert alert = driver.switchTo().alert();
//                log.warn("alert창 감지됨: {}", alert.getText());
//                alert.accept();
//                throw new RuntimeException("alert 발생으로 크롤링 중단: " + alert.getText());
//            } catch (NoAlertPresentException ignored) {}
//
//
//            WebElement priceElement = wait.until(
//                    ExpectedConditions.visibilityOfElementLocated(
//                            By.cssSelector("div[data-sentry-component='SalesPrice'] .price-amount")
//                    )
//            );
//
//            String priceText = priceElement.getText(); // 예: "19,400원"
//            log.info("추출된 쿠팡 가격 텍스트: {}", priceText);
//
//            String digits = priceText.replaceAll("[^0-9]", ""); // "19400"
//            if (digits.isEmpty()) {
//                log.error("쿠팡 가격에서 숫자 추출 실패: '{}'", priceText);
//                throw new RuntimeException("쿠팡 가격에서 숫자 추출 실패: " + priceText);
//            }
//
//            log.info("변환된 쿠팡 가격 (숫자): {}", digits);
//            return Integer.parseInt(digits);
//
//        } catch (Exception e) {
//            log.error("상품가격 요소를 찾는 데 시간 초과. URL: {}", itemUrl);
//            // 현재 페이지 소스를 출력하여 어떤 페이지가 로드되었는지 확인
//            String pageSource = driver.getPageSource();
//            log.error("페이지 소스 (전체): {}", pageSource); // 전체 긁어온 페이지 -- 디버깅
//
//            throw new RuntimeException("크롤링 서비스 중, 상품명을 가져오는도중 에러 발생 : " + e.getMessage());
//        } finally {
//            driver.quit();
//        }
//
//    }
//
//}
