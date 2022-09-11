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
import net.lightbody.bmp.BrowserMobProxy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductSchedule extends DriverBase {
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

    public ProductSchedule(CrawlRepository crawlRepository, ProductRepository productRepository, MailService mailService, BrowserMobProxy browserMobProxy) {
        this.crawlRepository = crawlRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
        this.browserMobProxy = browserMobProxy;
    }

    @Transactional
    @Scheduled(fixedDelay = 1200000)
    public void crawl() {
        instantiateDriverObject();
        List<CrawlUrl> crawlUrls = crawlRepository.findAll().stream()
                .filter(c -> c.getType().equals("exchange")).collect(Collectors.toList());
        List<Product> productList = productRepository.findAll();
        List<Product> productListExchange = productList.stream()
                .filter(p -> p.getType().equals("exchange")).collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<List<Product>>> futures = new ArrayList<>();
        crawlUrls.forEach(crawl -> {
            Future<List<Product>> listFutures = executorService.submit(() -> {
                WebDriver driver = getDriver(browserMobProxy, linkDriver);

                List<Product> productSampleCrawl = new ArrayList<>();
                List<Product> productUpdate = new ArrayList<>();
                try {
                    String url = crawl.getUrl();
                    driver.get(url);
                    DmxPage dmx = new DmxPage(browserMobProxy, linkDriver);
                    if (crawl.getType().equals("exchange")) {
                        String queryCrawl = crawl.getUrl().split("\\?")[1];
                        final Map<String, String> mapCrawl = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(queryCrawl);
                        String productType = mapCrawl.get("type");
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
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
                                    List<Product> productSendmailInternal = new ArrayList<>();
                                    listProductLinks.forEach(product -> {
                                        product.setType("exchange");
                                        product.setProductType(productType);
                                        if (productType.equals("7")) {
                                            product.setStatus("Hàng trưng bày");
                                        } else if (productType.equals("2")) {
                                            product.setStatus("Hàng đã sử dụng");
                                        }
                                        Optional<Product> productExchangeOpt = productListExchange.stream()
                                                .filter(p -> product.getProductType().equals(p.getProductType())
                                                        && product.getCode().equals(p.getCode())).findFirst();
                                        if (productExchangeOpt.isEmpty()) {
                                            Optional<Product> productOptExits = productSampleCrawl.stream()
                                                    .filter(p -> p.getCode().equals(product.getCode())).findFirst();
                                            if (productOptExits.isEmpty()) {
                                                if (productType.equals("7")) {
                                                    product.setStatus("Hàng trưng bày");
                                                } else if (productType.equals("2")) {
                                                    product.setStatus("Hàng đã sử dụng");
                                                }
                                                product.setStatusType("Hàng mới lên web");
                                                productSampleCrawl.add(product);
                                                productSendmailInternal.add(product);
                                            }
                                        } else {
                                            Product p = productExchangeOpt.get();
                                            if (productType.equals("7")) {
                                                p.setStatus("Hàng trưng bày");
                                            } else if (productType.equals("2")) {
                                                p.setStatus("Hàng đã sử dụng");
                                            }
                                            BigDecimal newPrice = product.getPrice();
                                            if (newPrice.compareTo(p.getPrice()) < 0) {
                                                p.setPriceOld(p.getPrice());
                                                p.setPrice(newPrice);
                                                productSendmailInternal.add(p);
                                            }
                                            product.setStatusType("Hàng cũ");
                                            productUpdate.add(p);
                                        }
                                    });
                                    if (!CollectionUtils.isEmpty(productSendmailInternal)) {
                                        if (!FIRST_RUN) {
                                            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                                                List<Product> productMail = productSendmailInternal.stream()
                                                        .filter(CollectionUtil.distinctByKey(Product::getCode))
                                                        .collect(Collectors.toList());
                                                ByteArrayInputStream in = ExcelUtils.productsToExcel(productMail);
                                                IOUtils.copy(in, out);
                                                File file = new File(linkFile);
                                                mailService.sendEmail(email, "Báo cáo sản phẩm giảm giá " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                                        "Danh sách sản phẩm giảm giá", Boolean.TRUE, Boolean.FALSE, file);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("Error productExchangeOpt !", e);
                                }
                            });
                        } catch (Exception e) {
                            log.error("Error exchange !", e);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(productUpdate)) {
                        productRepository.saveAll(productUpdate);
                    }
                    if (CollectionUtils.isNotEmpty(productSampleCrawl)) {
                        productRepository.saveAll(productSampleCrawl);
                    }
                    FIRST_RUN = false;
                } catch (Exception e) {
                    log.error("Error when get list product!", e);
                }
                return productUpdate;
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
