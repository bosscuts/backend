package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.entity.Product;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.repository.ProductRepository;
import com.bosscut.schedule.page_objects.DmxHomePage;
import com.bosscut.service.MailService;
import com.bosscut.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CrawlerSchedule extends DriverBase {

    private final CrawlRepository crawlRepository;
    private final ProductRepository productRepository;

    private final MailService mailService;

    @Value(value = "${application.path.chrome-driver}")
    private String linkDriver;

    public CrawlerSchedule(CrawlRepository crawlRepository, ProductRepository productRepository, MailService mailService) {
        this.crawlRepository = crawlRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
    }

    @Transactional
    @Scheduled(fixedDelay = 600000)
    public void crawl() throws Exception {
        System.out.println("Start time ===>>>: " + System.currentTimeMillis());
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();
        instantiateDriverObject();
        WebDriver driver = getDriver(linkDriver);
        List<Product> productList = productRepository.findAll();
        List<Product> productCrawl = new ArrayList<>();
        List<Product> productSendmail = new ArrayList<>();
        crawlUrls.forEach(crawl -> {
            try {
                String url = crawl.getUrl();
                driver.get(url);
                DmxHomePage dmx = new DmxHomePage(linkDriver);
                try {
                    int perPage = dmx.getListProduct().size();
                    int totalElement = dmx.getTotalElement();
                    int totalPage;
                    if ((totalElement % perPage) == 0) {
                        totalPage = (totalElement / perPage);
                    } else {
                        totalPage = (totalElement / perPage) + 1;
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
                    Product product = new Product();
                    String productCode = productElement.getDomAttribute("data-id");

                    Optional<Product> productServiceOpt = productList.stream()
                            .filter(p -> p.getProductCode().equals(productCode)).findFirst();
                    String price = "";
                    try {
                        product.setProductCode(productCode);
                        WebElement productNameElement = productElement.findElement(By.className("main-contain"))
                                .findElement(By.tagName("h3"));
                        if (Objects.nonNull(productNameElement)) {
                            String productName = productNameElement.getText();
                            product.setProductName(productName);
                        }
                    } catch (Exception e) {
                        log.error("Error when get productName!");
                    }
                    try {
                        WebElement priceElement = productElement.findElement(By.className("price"));
                        if (Objects.nonNull(priceElement)) {
                             price = priceElement.getText()
                                    .replace("₫", "")
                                    .replace(".", "")
                                    .replace(".", "");
                            product.setPrice(Integer.parseInt(StringUtils.trim(price)));
                        }
                    } catch (Exception e) {
                        log.error("Error when get price!");
                    }
                    try {
                        WebElement priceOldElement = productElement.findElement(By.className("price-old"));
                        if (Objects.nonNull(priceOldElement)) {
                            String priceOld = priceOldElement.getText()
                                    .replace("₫", "")
                                    .replace(".", "")
                                    .replace(".", "");
                            product.setPriceOld(Integer.parseInt(priceOld));
                        }
                    } catch (Exception e) {
                        log.error("Error when get priceOld!");
                    }
                    try {
                        WebElement percentElement = productElement.findElement(By.className("percent"));
                        if (Objects.nonNull(percentElement)) {
                            String percentStr = percentElement.getText()
                                    .replace("-", "")
                                    .replace("%", "");
                            int percent = Integer.parseInt(percentStr);
                            product.setPercentSale(percent);
                        }
                    } catch (Exception e) {
                        log.error("Error when get percent!");
                    }
                    if (productServiceOpt.isEmpty()) {
                        productCrawl.add(product);
                    } else {
                       Product p = productServiceOpt.get();
                       int newPrice = Integer.parseInt(price);
                       if (newPrice < p.getPrice()) {
                           p.setPrice(newPrice);
                       }
                       productCrawl.add(product);
                       productSendmail.add(p);
                    }
                });
            } catch (Exception e) {
                log.error("Error when get list product!");
            }
        });
        productRepository.saveAll(productCrawl);
        ByteArrayInputStream in = ExcelUtils.productsToExcel(productCrawl);

        IOUtils.copy(in, new FileOutputStream("/Users/hoadoan/Desktop/products.xlsx"));
        File file = new File("/Users/hoadoan/Desktop/products.xlsx");

//        mailService.sendEmail("project.devskill@gmail.com", "Báo cáo sản phẩm giảm giá", "Danh sách sản phẩm giảm giá", Boolean.FALSE, Boolean.FALSE, file);
        mailService.sendEmail("303thanhnguyen@gmail.com", "Báo cáo sản phẩm giảm giá", "Danh sách sản phẩm giảm giá", Boolean.FALSE, Boolean.FALSE, file);

        System.out.println("End time ===>>>: " + System.currentTimeMillis());
        clearCookies();
        closeDriverObjects();
    }
}
