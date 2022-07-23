package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.schedule.page_objects.DmxHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CrawlerSchedule extends DriverBase {

    private final CrawlRepository crawlRepository;

    public CrawlerSchedule(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }
    @Scheduled(fixedDelay = 100000)
    public void crawl() throws Exception {
        instantiateDriverObject();
        WebDriver driver = getDriver();
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();

        driver.get("https://www.dienmayxanh.com/dien-thoai");

        DmxHomePage dmx = new DmxHomePage();
        dmx.viewMore();

        List<WebElement> listProduct = dmx.getListProduct();
        listProduct.forEach(productElement -> {
            try {
                WebElement percentElement = productElement.findElement(By.className("percent"));
                if (Objects.nonNull(percentElement)) {
                    String percent = percentElement.getText();
                    System.out.println("percent ===>>>: " + percent);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        });

        clearCookies();
        closeDriverObjects();
    }
}
