package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.schedule.page_objects.DmxHomePage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class CrawlerSchedule extends DriverBase {

    private final CrawlRepository crawlRepository;

    public CrawlerSchedule(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }
    @Scheduled(fixedDelay = 500000)
    public void crawl() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();
        instantiateDriverObject();
        WebDriver driver = getDriver();



        crawlUrls.forEach(crawl -> {
            try {
                String url = crawl.getUrl();
                driver.get(url);
                DmxHomePage dmx = new DmxHomePage();
                int totalElement = dmx.getTotalElement();
                String pageTitle = dmx.getPageTitle();
                log.info("Page title {}!", pageTitle);
                log.info("Total element {}!", totalElement);
//                dmx.viewMore();

                List<WebElement> listProduct = dmx.getListProduct();
                listProduct.forEach(productElement -> {
                    try {
                        WebElement percentElement = productElement.findElement(By.className("percent"));
                        if (Objects.nonNull(percentElement)) {
                            String percent = percentElement.getText();
                            System.out.println("percent ===>>>: " + percent);
                        }
                    } catch (Exception e) {
                        log.error("Error when get percent!");
                    }
                });
            } catch (Exception e) {
                log.error("Error when get list product!");
            }
        });
//        executorService.execute(() -> {
//
//        });
//        executorService.shutdown();

        clearCookies();
        closeDriverObjects();
    }
}
