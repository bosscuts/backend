package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.entity.Product;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.repository.ProductRepository;
import com.bosscut.schedule.page_objects.DmxPage;
import com.bosscut.service.MailService;
import com.bosscut.util.CollectionUtil;
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
    public void crawl() {
        instantiateDriverObject();
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();
        List<Product> productList = productRepository.findAll();
        List<Product> productListExchange = productList.stream()
                .filter(p -> p.getType().equals("exchange")).collect(Collectors.toList());
        WebDriver driver = getDriver(linkDriver);
        List<Product> productCrawl = new ArrayList<>();
        List<Product> productSampleCrawl = new ArrayList<>();
        List<Product> productUpdate = new ArrayList<>();
        List<Product> productSendmail = new ArrayList<>();
        System.out.println("Start time : " + LocalDateTime.now());
        crawlUrls.forEach(crawl -> {
            try {
                String url = crawl.getUrl();
                driver.get(url);
                DmxPage dmx = new DmxPage(linkDriver);
                if (crawl.getType().equals("normal")) {
                    try {
                        for (int i = 0; i <= 1000; i++) {
                            TimeUnit.MILLISECONDS.sleep(300);
                            dmx.viewMore();
                            TimeUnit.MILLISECONDS.sleep(300);
                        }
                    } catch (Exception e) {
                        log.error("Error calculate totalPage!");
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
                if (crawl.getType().equals("exchange")) {
                    String queryCrawl = crawl.getUrl().split("\\?")[1];
                    final Map<String, String> mapCrawl = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(queryCrawl);
                    String productType = mapCrawl.get("type");
                    try {
                        dmx.closePopup();
                    } catch (Exception e) {
                        log.error("Error close popup!");
                    }
                    try {
                        List<String> productLinks = dmx.getListProductExchange();
                        productLinks.forEach(prodLink -> {
                            try {
                                driver.get(prodLink);
                                List<Product> listProductLinks = dmx.getListProductExchange1();
                                listProductLinks.forEach(product -> {
                                    product.setType("exchange");
                                    product.setProductType(productType);
                                    if (productType.equals("7")) {
                                        product.setStatus("Hàng trưng bày");
                                    } else if (productType.equals("2")) {
                                        product.setStatus("Hàng đã sử dụng");
                                    }
                                    Optional<Product> productExchangeOpt = productListExchange.stream()
                                            .filter(p -> product.getProductCode().equals(p.getProductCode())
                                                    && product.getProductType().equals(p.getProductType())
                                                    && product.getCode().equals(p.getCode())).findFirst();
                                    if (productExchangeOpt.isEmpty()) {
                                        productSampleCrawl.add(product);
                                        productSendmail.add(product);
                                    } else {
                                        Product p = productExchangeOpt.get();
                                        float newPrice = product.getPrice();
                                        if (newPrice < p.getPrice()) {
                                            p.setPriceOld(p.getPrice());
                                            p.setPrice(newPrice);
                                            if (productType.equals("7")) {
                                                p.setStatus("Hàng trưng bày");
                                            } else if (productType.equals("2")) {
                                                p.setStatus("Hàng đã sử dụng");
                                            }
                                            productUpdate.add(p);
                                            productSendmail.add(p);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                log.error("Error productExchangeOpt !", e);
                            }
                        });
                    } catch (Exception e) {
                        log.error("Error calculate totalPage Exchange !", e);
                    }
                }

            } catch (Exception e) {
                log.error("Error when get list product!", e);
            }
        });
        System.out.println("End time : " + LocalDateTime.now());
        if (CollectionUtils.isNotEmpty(productUpdate)) {
            productRepository.saveAll(productUpdate);
        }
        if (CollectionUtils.isNotEmpty(productSampleCrawl)) {
            List<Product> productSampleCrawls = productSampleCrawl.stream()
                    .filter(CollectionUtil.distinctByKey(Product::getCode))
                    .collect(Collectors.toList());
            productRepository.saveAll(productSampleCrawls);
        }
        if (CollectionUtils.isNotEmpty(productCrawl)) {
            productRepository.saveAll(productCrawl);
        }
        LocalDateTime now = LocalDateTime.now();

        if (!CollectionUtils.isEmpty(productCrawl)) {
            if (!FIRST_RUN) {
                try (FileOutputStream out = new FileOutputStream(linkFileProductNew)) {
                    List<Product> productCrawls = productCrawl.stream()
                            .filter(CollectionUtil.distinctByKey(Product::getCode))
                            .collect(Collectors.toList());

                    ByteArrayInputStream in = ExcelUtils.newProductsToExcel(productCrawls);
                    IOUtils.copy(in, out);
                    File file = new File(linkFileProductNew);
                    mailService.sendEmail(email, "Báo cáo sản phẩm mới " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            "Danh sách sản phẩm mới", Boolean.TRUE, Boolean.FALSE, file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!CollectionUtils.isEmpty(productSendmail)) {
            if (!FIRST_RUN) {
                try (FileOutputStream out = new FileOutputStream(linkFile)) {
                    List<Product> productSendmails = productSendmail.stream()
                            .filter(CollectionUtil.distinctByKey(Product::getCode))
                            .collect(Collectors.toList());

                    ByteArrayInputStream in = ExcelUtils.productsToExcel(productSendmails);
                    IOUtils.copy(in, out);
                    File file = new File(linkFile);
                    mailService.sendEmail(email, "Báo cáo sản phẩm giảm giá " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            "Danh sách sản phẩm giảm giá", Boolean.TRUE, Boolean.FALSE, file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        FIRST_RUN = false;

        clearCookies();
        closeDriverObjects();
    }
}
