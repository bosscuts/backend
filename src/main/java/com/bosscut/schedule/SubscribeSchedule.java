package com.bosscut.schedule;

import com.bosscut.entity.Category;
import com.bosscut.entity.Product;
import com.bosscut.repository.CategoryRepository;
import com.bosscut.repository.ProductRepository;
import com.bosscut.schedule.page_objects.DmxPage;
import com.bosscut.service.MailService;
import com.bosscut.util.ExcelUtils;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SubscribeSchedule extends DriverBase {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;
    @Value(value = "${application.path.chrome-driver}")
    private String linkDriver;
    @Value(value = "${application.path.file-download}")
    private String linkFile;
    @Value(value = "${application.email}")
    private String email;
    public static Boolean FIRST_RUN = true;

    public SubscribeSchedule(CategoryRepository categoryRepository, ProductRepository productRepository, MailService mailService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
    }

    @Transactional
    @Scheduled(fixedDelay = 250000)
    public void subscribeCategory() throws Exception {
        log.info("Start time subscribeCategory ===>>>: " + new Date());
        List<Category> categories = categoryRepository.findAll();
        instantiateDriverObject();
        WebDriver driver = getDriver(linkDriver);
        List<Product> productList = productRepository.findAll();
        List<Product> productCrawl = new ArrayList<>();
        categories.forEach(category -> {
            try {
                String url = category.getUrl();
                driver.get(url);
                DmxPage dmx = new DmxPage(linkDriver);
                try {
                    for (int i = 0; i <= 1000; i++) {
                        dmx.viewMore();
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (Exception e) {
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
                        WebElement productLinkElement = productElement.findElement(By.className("main-contain"));
                        if (Objects.nonNull(productLinkElement)) {
                            String productLink = productLinkElement.getDomAttribute("href");
                            product.setLink("https://www.dienmayxanh.com" + productLink);
                        }
                    } catch (Exception e) {
                        log.error("Error when get productLink!");
                    }
                    product.setType("normal");
                    if (productOpt.isEmpty()) {
                        productCrawl.add(product);
                    }
                });
            } catch (Exception e) {
                log.error("Error when get list product!");
            }
        });
        if (CollectionUtils.isNotEmpty(productCrawl)) {
            productRepository.saveAll(productCrawl);
            if (!FIRST_RUN) {
                try (FileOutputStream out = new FileOutputStream(linkFile)) {
                    ByteArrayInputStream in = ExcelUtils.newProductsToExcel(productCrawl);
                    IOUtils.copy(in, out);
                    File file = new File(linkFile);
                    mailService.sendEmail(email, "Báo cáo sản phẩm mới " + LocalDateTime.now(), "Danh sách sản phẩm mới", Boolean.TRUE, Boolean.FALSE, file);
                }
            }
        }

        FIRST_RUN = false;
        log.info("End time subscribeCategory ===>>>: " + new Date());
        clearCookies();
        closeDriverObjects();
    }
}
