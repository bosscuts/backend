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
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CrawlerSchedule extends DriverBase {

    private final CrawlRepository crawlRepository;

    public CrawlerSchedule(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }
    @Scheduled(fixedDelay = 600000)
    public void crawl() throws Exception {
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();
        instantiateDriverObject();
        WebDriver driver = getDriver();

        crawlUrls.forEach(crawl -> {
            try {
                String url = crawl.getUrl();
                driver.get(url);
                DmxHomePage dmx = new DmxHomePage();
                try {
                    int perPage = dmx.getListProduct().size();
                    int totalElement = dmx.getTotalElement();
                    int totalPage;
                    if ((totalElement % perPage) == 0) {
                        totalPage = (totalElement/perPage);
                    } else {
                        totalPage = (totalElement/perPage) + 1;
                    }
                    for (int i = 0; i <= totalPage; i++) {
                        TimeUnit.SECONDS.sleep(1);
                        dmx.viewMore();
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (Exception e) {
                    log.error("Error calculate totalPage!");
                }

                List<WebElement> listProduct = dmx.getListProduct();
                listProduct.forEach(productElement -> {
                    try {
                        WebElement percentElement = productElement.findElement(By.className("percent"));
                        WebElement productNameElement = productElement.findElement(By.className("main-contain"))
                                .findElement(By.tagName("h3"));
                        if (Objects.nonNull(percentElement)) {
                            String percentStr = percentElement.getText()
                                    .replace("-", "")
                                    .replace("%", "");
                            int percent = Integer.parseInt(percentStr);

                            if (percent > 40) {
                                log.info("productName ===>>>: {}", productNameElement.getText());
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error when get percent!");
                    }
                });
            } catch (Exception e) {
                log.error("Error when get list product!");
            }
        });
        clearCookies();
        closeDriverObjects();
    }
}
