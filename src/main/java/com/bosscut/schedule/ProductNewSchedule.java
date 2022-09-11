package com.bosscut.schedule;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.entity.Product;
import com.bosscut.repository.CrawlRepository;
import com.bosscut.repository.ProductRepository;
import com.bosscut.schedule.page_objects.DmxPage;
import com.bosscut.service.MailService;
import com.bosscut.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductNewSchedule extends DriverBase {
    private final CrawlRepository crawlRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;
    private final BrowserMobProxy browserMobProxy;
    @Value(value = "${application.path.chrome-driver}")
    private String linkDriver;
    @Value(value = "${application.path.file-download}")
    private String linkFile;
    @Value(value = "${application.email}")
    private String email;
    public static Boolean FIRST_RUN = true;

    public ProductNewSchedule(CrawlRepository crawlRepository, ProductRepository productRepository, MailService mailService, BrowserMobProxy browserMobProxy) {
        this.crawlRepository = crawlRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
        this.browserMobProxy = browserMobProxy;
    }

    @Transactional
    @Scheduled(fixedDelay = 150000)
    public void crawl() {
        instantiateDriverObject();
        List<CrawlUrl> crawlUrls = crawlRepository.findAll().stream()
                .filter(c -> c.getType().equals("normal")).collect(Collectors.toList());
        List<Product> productList = productRepository.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<List<Product>>> futures = new ArrayList<>();
        crawlUrls.forEach(crawl -> {
            Future<List<Product>> listFutures = executorService.submit(() -> {
                List<Product> productCrawl = new ArrayList<>();
                List<Product> productUpdate = new ArrayList<>();
                List<Product> productSendmail = new ArrayList<>();
                try {
                    WebDriver driver = getDriver(browserMobProxy, linkDriver);
                    String url = crawl.getUrl();
                    driver.get(url);
                    DmxPage dmx = new DmxPage(browserMobProxy, linkDriver);
                    if (crawl.getType().equals("normal")) {
                        try {
                            for (int i = 0; i <= 1000; i++) {
                                TimeUnit.MILLISECONDS.sleep(300);
                                dmx.viewMore();
                                TimeUnit.MILLISECONDS.sleep(300);
                            }
                        } catch (Exception e) {
                            log.error("Error viewMore!");
                        }

                        List<WebElement> listProduct = dmx.getListProduct();
                        listProduct.forEach(productElement -> {
                            Product product = new Product();
                            String productCode = productElement.getDomAttribute("data-id");

                            Optional<Product> productOpt = productList.stream()
                                    .filter(p -> productCode.equals(p.getProductCode())).findFirst();
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
                                    product.setPrice(new BigDecimal(StringUtils.trim(price)));
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
                                    product.setPriceOld(new BigDecimal(StringUtils.trim(priceOld)));
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
                            try {
                                product.setType("normal");
                                if (productOpt.isEmpty()) {
                                    Optional<Product> productOptExits = productCrawl.stream()
                                            .filter(p -> product.getProductCode().equals(p.getProductCode())).findFirst();
                                    if (productOptExits.isEmpty()) {
                                        productCrawl.add(product);
                                    }
                                } else {
                                    Product p = productOpt.get();
                                    p.setStatus("Hàng mới");
                                    BigDecimal newPrice = new BigDecimal(StringUtils.trim(price));
                                    if (newPrice.compareTo(p.getPrice()) < 0) {
                                        p.setPrice(newPrice);
                                        productSendmail.add(p);
                                        productUpdate.add(p);
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Error when add productSendmail!");
                            }
                        });
                        if (!CollectionUtils.isEmpty(productSendmail)) {
                            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                                ByteArrayInputStream in = ExcelUtils.productsToExcel(productSendmail);
                                IOUtils.copy(in, out);
                                File file = new File(linkFile);
                                mailService.sendEmail(email, "Báo cáo sản phẩm giảm giá " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                        "Danh sách sản phẩm giảm giá", Boolean.TRUE, Boolean.FALSE, file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(productUpdate)) {
                            productRepository.saveAll(productUpdate);
                        }
                        if (CollectionUtils.isNotEmpty(productCrawl)) {
                            productRepository.saveAll(productCrawl);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error when get list product!", e);
                }
                return productCrawl;
            });
            futures.add(listFutures);
        });
        futures.forEach(ft -> {
            try {
                List<Product> products = ft.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        clearCookies();
        closeDriverObjects();
        executorService.shutdown();
    }
}
