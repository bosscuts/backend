package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.entity.Product;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.repository.ProductRepository;
import com.bosscut.schedule.page_objects.DmxPage;
import com.bosscut.service.MailService;
import com.bosscut.util.ExcelUtils;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SubscribeSchedule extends DriverBase {
    private final CrawlRepository crawlRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;

    @Value(value = "${application.path.chrome-driver}")
    private String linkDriver;

    @Value(value = "${application.path.file-download}")
    private String linkFile;

    public SubscribeSchedule(CrawlRepository crawlRepository, ProductRepository productRepository, MailService mailService) {
        this.crawlRepository = crawlRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
    }

    @Transactional
    @Scheduled(fixedDelay = 600000)
    public void crawl() throws Exception {
        log.info("Start time ===>>>: " + new Date());
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();
        instantiateDriverObject();
        WebDriver driver = getDriver(linkDriver);
        List<Product> productList = productRepository.findAll();
        List<Product> productListExchange = productList.stream()
                .filter(p -> p.getType().equals("exchange")).collect(Collectors.toList());
        List<Product> productCrawl = new ArrayList<>();
        List<Product> productSendmail = new ArrayList<>();
        crawlUrls.forEach(crawl -> {
            try {
                String url = crawl.getUrl();
                driver.get(url);
                DmxPage dmx = new DmxPage(linkDriver);
                if (crawl.getType().equals("exchange")) {
                    try {
                        dmx.closePopup();
                    } catch (Exception e) {
                        log.error("Error close popup!");
                    }
                    try {
                        for (int i = 0; i < 1000; i++) {
                            TimeUnit.SECONDS.sleep(1);
                            dmx.viewMoreExchange();
                            TimeUnit.SECONDS.sleep(1);
                        }
                    } catch (Exception e) {
                        log.error("Error calculate totalPage exchange!");
                        TimeUnit.SECONDS.sleep(1);
                    }
                    try {
                        List<WebElement> listProductExchange = dmx.getListProductExchange();
                        listProductExchange.forEach(productElement -> {
                            Product product = new Product();
                            product.setType("exchange");
                            product.setStatus("Hàng trưng bày");
                            String price = "";
                            try {
                                WebElement productNameElement = productElement.findElement(By.className("prdName"));
                                if (Objects.nonNull(productNameElement)) {
                                    String productName = productNameElement.getText();
                                    product.setProductName(productName);
                                }
                            } catch (Exception e) {
                                log.error("Error when get productName!");
                            }

                            try {
                                WebElement productLinkElement = productElement.findElement(By.tagName("a"));
                                if (Objects.nonNull(productLinkElement)) {
                                    String productLink = productLinkElement.getDomAttribute("href");
                                    product.setLink("https://www.dienmayxanh.com" + productLink);
                                }
                                String query = product.getLink().split("\\?")[1];
                                final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
                                String productCode = map.get("pid");
                                product.setProductCode(productCode);

                            } catch (Exception e) {
                                log.error("Error when get productName!");
                            }
                            try {
                                WebElement priceElement = productElement.findElement(By.tagName("a")).findElement(By.tagName("strong"));
                                if (Objects.nonNull(priceElement)) {
                                    price = priceElement.getText()
                                            .replace("₫", "")
                                            .replace(".", "")
                                            .replace(".", "");
                                    product.setPrice(Float.parseFloat(StringUtils.trim(price)));
                                }
                            } catch (Exception e) {
                                log.error("Error when get price!");
                            }
                            try {
                                WebElement priceOldElement = productElement.findElement(By.tagName("a")).findElement(By.tagName("cite"));
                                if (Objects.nonNull(priceOldElement)) {
                                    String priceOld = priceOldElement.getText()
                                            .replace("₫", "")
                                            .replace(".", "")
                                            .replace(".", "");
                                    product.setPriceOld(Float.parseFloat(priceOld));
                                }
                            } catch (Exception e) {
                                log.error("Error when get priceOld!");
                            }

                            try {
                                WebElement percentElement = productElement.findElement(By.tagName("a")).findElement(By.cssSelector("p:nth-child(7) > span"));
                                if (Objects.nonNull(percentElement)) {
                                    String percentStr = percentElement.getText();
                                    product.setPercentSaleString(percentStr);
                                }
                            } catch (Exception e) {
                                log.error("Error when get percent!");
                            }

                            Optional<Product> productExchangeOpt = productListExchange.stream()
                                    .filter(p -> p.getProductCode().equals(product.getProductCode())).findFirst();

                            if (productExchangeOpt.isEmpty()) {
                                productCrawl.add(product);
                            } else {
                                Product p = productExchangeOpt.get();
                                float newPrice = Float.parseFloat(price);
                                if (newPrice < p.getPrice()) {
                                    p.setPrice(newPrice);
                                    productSendmail.add(p);
                                }
                            }
                        });
                    } catch (Exception e) {
                        log.error("Error calculate totalPage Exchange !", e);
                    }
                } else {
                    try {
                        for (int i = 0; i <= 1000; i++) {
                            TimeUnit.SECONDS.sleep(1);
                            dmx.viewMore();
                            TimeUnit.SECONDS.sleep(1);
                        }
                    } catch (Exception e) {
                        log.error("Error calculate totalPage!");
                        TimeUnit.SECONDS.sleep(1);
                    }

                    List<WebElement> listProduct = dmx.getListProduct();
                    listProduct.forEach(productElement -> {
                        Product product = new Product();
                        String productCode = productElement.getDomAttribute("data-id");

                        Optional<Product> productOpt = productList.stream()
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
                                product.setPrice(Float.parseFloat(StringUtils.trim(price)));
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
                                product.setPriceOld(Float.parseFloat(priceOld));
                            }
                        } catch (Exception e) {
                            log.error("Error when get priceOld!");
                        }
                        try {
                            WebElement percentElement = productElement.findElement(By.className("percent"));
                            if (Objects.nonNull(percentElement)) {
                                String percentStr = percentElement.getText()
                                        .replace("-", "");
                                product.setPercentSaleString(percentStr);
                            }
                        } catch (Exception e) {
                            log.error("Error when get percent!");
                        }
                        try {
                            WebElement productLinkElement = productElement.findElement(By.className("main-contain"));
                            if (Objects.nonNull(productLinkElement)) {
                                String productLink = productLinkElement.getDomAttribute("href");
                                product.setLink("https://www.dienmayxanh.com" + productLink);
                            }
                        } catch (Exception e) {
                            log.error("Error when get productName!");
                        }
                        product.setType("normal");
                        product.setStatus("Hàng mới");

                        if (productOpt.isEmpty()) {
                            productCrawl.add(product);
                        } else {
                            Product p = productOpt.get();
                            p.setStatus("Hàng mới");
                            float newPrice = Float.parseFloat(price);
                            if (newPrice < p.getPrice()) {
                                p.setPrice(newPrice);
                                productSendmail.add(p);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                log.error("Error when get list product!");
            }
        });
        productRepository.saveAll(productCrawl);
        if (!CollectionUtils.isEmpty(productSendmail)) {
            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                ByteArrayInputStream in = ExcelUtils.productsToExcel(productSendmail);
                IOUtils.copy(in, out);
                File file = new File(linkFile);
                mailService.sendEmail("project.devskill@gmail.com", "Báo cáo sản phẩm giảm giá " + new Date(), "Danh sách sản phẩm giảm giá", Boolean.TRUE, Boolean.FALSE, file);
            }
        }

        log.info("End time ===>>>: " + new Date());
        clearCookies();
        closeDriverObjects();
    }
}
