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

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductNewSchedule extends DriverBase {
    private final CrawlRepository crawlRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;
    @Value(value = "${application.path.chrome-driver}")
    private String linkDriver;
    @Value(value = "${application.path.file-download}")
    private String linkFile;
    @Value(value = "${application.path.new-product-file}")
    private String linkFileProductNew;
    @Value(value = "${application.email}")
    private String email;
    public static Boolean FIRST_RUN = true;

    public ProductNewSchedule(CrawlRepository crawlRepository, ProductRepository productRepository, MailService mailService) {
        this.crawlRepository = crawlRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
    }

    @Transactional
    @Scheduled(fixedDelay = 600000)
    public void crawl() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {

            log.info("Start time crawl ===>>>: " + new Date());
            List<CrawlUrl> crawlUrls = crawlRepository.findAll();
            instantiateDriverObject();
            WebDriver driver = getDriver(linkDriver);
            List<Product> productList = productRepository.findAll();
            List<Product> productListExchange = productList.stream()
                    .filter(p -> p.getType().equals("exchange")).collect(Collectors.toList());
            List<Product> productCrawl = new ArrayList<>();
            List<Product> productSampleCrawl = new ArrayList<>();
            List<Product> productSendmail = new ArrayList<>();
            List<Product> productUpdate = new ArrayList<>();
            crawlUrls.forEach(crawl -> {
                try {
                    String url = crawl.getUrl();
                    driver.get(url);
                    DmxPage dmx = new DmxPage(linkDriver);
                    if (crawl.getType().equals("normal")) {
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
                                    productUpdate.add(p);
                                }
                            }
                        });
                    }
//                    if (crawl.getType().equals("exchange")) {
//                        String queryCrawl = crawl.getUrl().split("\\?")[1];
//                        final Map<String, String> mapCrawl = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(queryCrawl);
//                        String productType = mapCrawl.get("type");
//                        try {
//                            dmx.closePopup();
//                        } catch (Exception e) {
//                            log.error("Error close popup!");
//                        }
//                        try {
//                            List<String> productLinks = dmx.getListProductExchange();
//                            productLinks.forEach(prodLink -> {
//                                driver.get(prodLink);
//                                try {
//                                    for (int i = 0; i < 1000; i++) {
//                                        TimeUnit.SECONDS.sleep(1);
//                                        dmx.viewMoreExchange();
//                                        TimeUnit.SECONDS.sleep(1);
//                                    }
//                                } catch (Exception e) {
//                                    log.error("Error productLinks!");
//                                }
//
//                                List<WebElement> listProductLinks = dmx.getListProductExchange1();
//                                listProductLinks.forEach(productExchangeElement -> {
//                                    Product product = new Product();
//                                    product.setType("exchange");
//                                    product.setProductType(productType);
//                                    if (productType.equals("7")) {
//                                        product.setStatus("Hàng trưng bày");
//                                    } else if (productType.equals("2")) {
//                                        product.setStatus("Hàng đã sử dụng");
//                                    }
//                                    String price = "";
//                                    try {
//                                        WebElement productNameElement = productExchangeElement.findElement(By.className("prdName"));
//                                        if (Objects.nonNull(productNameElement)) {
//                                            String productName = productNameElement.getText();
//                                            product.setProductName(productName);
//                                        }
//                                    } catch (Exception e) {
//                                        log.error("Error when get productName!");
//                                    }
//
//                                    try {
//                                        WebElement productLinkElement = productExchangeElement.findElement(By.tagName("a"));
//                                        if (Objects.nonNull(productLinkElement)) {
//                                            String productLinkElementStr = productLinkElement.getDomAttribute("onclick");
//                                            String[] arrProductLink = productLinkElementStr.split(",");
//                                            String prodUrl = arrProductLink[2]
//                                                    .replace("'", "")
//                                                    .replace("(", "")
//                                                    .replace(")", "");
//                                            String code = prodUrl.split("#")[1];
//                                            product.setLink("https://www.dienmayxanh.com/" + prodUrl);
//                                            product.setCode(code);
//                                        }
//                                        String query = product.getLink().split("\\?")[1];
//                                        final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
//                                        String productCode = map.get("pid");
//                                        product.setProductCode(productCode);
//
//                                    } catch (Exception e) {
//                                        log.error("Error when get productName!");
//                                    }
//                                    try {
//                                        WebElement priceElement = productExchangeElement.findElement(By.tagName("a")).findElement(By.tagName("strong"));
//                                        if (Objects.nonNull(priceElement)) {
//                                            price = priceElement.getText()
//                                                    .replace("₫", "")
//                                                    .replace(".", "")
//                                                    .replace(".", "");
//                                            product.setPrice(Float.parseFloat(StringUtils.trim(price)));
//                                        }
//                                    } catch (Exception e) {
//                                        log.error("Error when get price!");
//                                    }
//                                    try {
//                                        WebElement percentElement = productExchangeElement.findElement(By.tagName("a")).findElement(By.cssSelector("label"));
//                                        if (Objects.nonNull(percentElement)) {
//                                            String percentStr = percentElement.getText();
//                                            product.setPercentSaleString(percentStr);
//                                        }
//                                    } catch (Exception e) {
//                                        log.error("Error when get percent!");
//                                    }
//
//                                    Optional<Product> productExchangeOpt = productListExchange.stream()
//                                            .filter(p -> p.getProductCode().equals(product.getProductCode())
//                                                    && p.getProductType().equals(product.getProductType())
//                                                    && p.getCode().equals(product.getCode())).findFirst();
//                                    if (productExchangeOpt.isEmpty()) {
//                                        productSampleCrawl.add(product);
//                                    } else {
//                                        Product p = productExchangeOpt.get();
//                                        float newPrice = Float.parseFloat(price);
//                                        if (newPrice < p.getPrice()) {
//                                            p.setPriceOld(p.getPrice());
//                                            p.setPrice(newPrice);
//                                            if (productType.equals("7")) {
//                                                p.setStatus("Hàng trưng bày");
//                                            } else if (productType.equals("2")) {
//                                                p.setStatus("Hàng đã sử dụng");
//                                            }
//                                            productUpdate.add(p);
//                                            productSendmail.add(p);
//                                        }
//                                    }
//                                });
//                            });
//                        } catch (Exception e) {
//                            log.error("Error calculate totalPage Exchange !", e);
//                        }
//                    }
                } catch (Exception e) {
                    log.error("Error when get list product!", e);
                }
            });
            if (CollectionUtils.isNotEmpty(productUpdate)) {
                productRepository.saveAll(productUpdate);
            }
            if (CollectionUtils.isNotEmpty(productSampleCrawl)) {
                productRepository.saveAll(productSampleCrawl);
            }
            if (CollectionUtils.isNotEmpty(productCrawl)) {
                productRepository.saveAll(productCrawl);
            }
            LocalDateTime now = LocalDateTime.now();
            if (!CollectionUtils.isEmpty(productSendmail)) {
                if (!FIRST_RUN) {
                    try (FileOutputStream out = new FileOutputStream(linkFile)) {
                        ByteArrayInputStream in = ExcelUtils.productsToExcel(productSendmail);
                        IOUtils.copy(in, out);
                        File file = new File(linkFile);
                        mailService.sendEmail(email, "Báo cáo sản phẩm giảm giá " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                "Danh sách sản phẩm giảm giá", Boolean.TRUE, Boolean.FALSE, file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(productCrawl)) {
                if (!FIRST_RUN) {
                    try (FileOutputStream out = new FileOutputStream(linkFileProductNew)) {
                        ByteArrayInputStream in = ExcelUtils.newProductsToExcel(productCrawl);
                        IOUtils.copy(in, out);
                        File file = new File(linkFileProductNew);
                        mailService.sendEmail(email, "Báo cáo sản phẩm mới " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                "Danh sách sản phẩm mới", Boolean.TRUE, Boolean.FALSE, file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            FIRST_RUN = false;
            log.info("End time crawl ===>>>: " + new Date());
            clearCookies();
            closeDriverObjects();
        });

    }
}
