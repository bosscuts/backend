package com.bosscut.schedule.page_objects;

import com.bosscut.entity.Product;
import com.bosscut.schedule.DriverBase;
import com.google.common.base.Splitter;
import com.lazerycode.selenium.util.Query;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

public class DmxPage {

    private final Query listProduct = new Query().defaultLocator(By.className("listproduct"));
    private final Query listProductExchange = new Query().defaultLocator(By.className("lstcate"));
    private final Query listProductExchange1 = new Query().defaultLocator(By.className("cate"));
    private final Query closePopup = new Query().defaultLocator(By.className("dong"));
    private final Query place = new Query().defaultLocator(By.className("place"));
    private final Query viewMore = new Query().defaultLocator(By.className("view-more"));
    private final Query viewMoreExchange = new Query().defaultLocator(By.className("viewmore"));

    private final Query provinceClick1 = new Query().defaultLocator(
            By.cssSelector("div.scroll > aside > a:nth-child(3)")
    );
    private final Query provinceClick2 = new Query().defaultLocator(
            By.cssSelector("div.scroll > aside > a:nth-child(6)")
    );
    private final Query provinceClick3 = new Query().defaultLocator(
            By.cssSelector("div.scroll > aside > a:nth-child(9)")
    );
    private final Query provinceClick4 = new Query().defaultLocator(
            By.cssSelector("div.scroll > aside > a:nth-child(62)"));

    private final Query provinceClickAll = new Query().defaultLocator(
            By.cssSelector("div.scroll > aside > a:nth-child(1)")
    );

    private final WebDriverWait wait;
    private final RemoteWebDriver driver;

    public DmxPage(String pathDriver) {
        driver = DriverBase.getDriver(pathDriver);
        initQueryObjects(this, driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofMillis(100));
    }

    public void clickTivi() {
        listProduct.findWebElement().click();
    }

    public List<WebElement> getListProduct() {
        return listProduct.findWebElement().findElements(By.tagName("li"));
    }

    public List<String> getListProductExchange() {
        List<String> productLinks = new ArrayList<>();
        List<Query> provinceClicks = List.of(provinceClick1, provinceClick2, provinceClick3, provinceClick4);

        provinceClicks.forEach(provinceClick -> {
            try {
                place.findWebElement().click();
                TimeUnit.MILLISECONDS.sleep(200);
                provinceClick.findWebElement().click();
                try {
                    for (int i = 0; i < 1000; i++) {
                        TimeUnit.MILLISECONDS.sleep(200);
                        viewMoreExchange();
                        TimeUnit.MILLISECONDS.sleep(200);
                    }
                } catch (Exception e) {
                    System.out.println("Error viewMoreExchange! " + e.getMessage());
                }
                List<WebElement> productElement = listProductExchange.findWebElement().findElements(By.tagName("li"));
                for (WebElement product : productElement) {
                    WebElement linkProd = product.findElement(By.tagName("a"));
                    String productLink = linkProd.getDomAttribute("href");
                    productLinks.add("https://www.dienmayxanh.com" + productLink);
                }
            } catch (Exception e) {
                System.out.println("Error listProductExchange! " + e.getMessage());
            }
        });
        return productLinks;
    }

    public List<Product> getListProductExchange1() {
        List<Query> provinceClicks = List.of(provinceClick1, provinceClick2, provinceClick3, provinceClick4);
        List<Product> products = new ArrayList<>();
        try {
            place.findWebElement().click();
            TimeUnit.MILLISECONDS.sleep(100);
            provinceClickAll.findWebElement().click();
        } catch (Exception e) {
            System.out.println("Error provinceClickAll! " + e.getMessage());
        }
        provinceClicks.forEach(provinceClick -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                place.findWebElement().click();
                TimeUnit.MILLISECONDS.sleep(200);
                provinceClick.findWebElement().click();
            } catch (Exception e) {
                System.out.println("Error provinceClick getListProductExchange1! " + e.getMessage());
            }
            try {
                for (int i = 0; i < 1000; i++) {
                    TimeUnit.MILLISECONDS.sleep(200);
                    viewMoreExchange();
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (Exception e) {
                System.out.println("Error viewMoreExchange getListProductExchange1! " + e.getMessage());
            }
            List<WebElement> cates = listProductExchange1.findWebElements();
            cates.forEach(cate -> {
                List<WebElement> productElements = cate.findElements(By.tagName("li"));
                productElements.forEach(productElement -> {
                    Product product = new Product();
                    try {
                        WebElement productNameElement = productElement.findElement(By.className("prdName"));
                        if (Objects.nonNull(productNameElement)) {
                            String productName = productNameElement.getText();
                            product.setProductName(productName);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        WebElement productLinkElement = productElement.findElement(By.tagName("a"));
                        if (Objects.nonNull(productLinkElement)) {
                            String productLinkElementStr = productLinkElement.getDomAttribute("onclick");
                            String[] arrProductLink = productLinkElementStr.split(",");
                            String prodUrl = arrProductLink[2]
                                    .replace("'", "")
                                    .replace("(", "")
                                    .replace(")", "");
                            String code = prodUrl.split("#")[1];
                            product.setLink("https://www.dienmayxanh.com/may-doi-tra/" + prodUrl);
                            product.setCode(code);
                        }
                        String query = product.getLink().split("\\?")[1];
                        final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
                        String productCode = map.get("pid");
                        product.setProductCode(productCode);

                    } catch (Exception e) {
                        System.out.println("Error when get productCode!");
                    }
                    String price = "";

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
                        System.out.println("Error when get price!");
                    }

                    try {
                        WebElement percentElement = productElement.findElement(By.tagName("a")).findElement(By.cssSelector("label"));
                        if (Objects.nonNull(percentElement)) {
                            String percentStr = percentElement.getText();
                            product.setPercentSaleString(percentStr);
                        }
                    } catch (Exception e) {
                        System.out.println("Error when get percent!");
                    }
                    products.add(product);
                });
            });
        });
        return products;
    }

    public void viewMore() {
        viewMore.findWebElement().click();
    }

    public void closePopup() {
        closePopup.findWebElement().click();
    }

    public void viewMoreExchange() {
        viewMoreExchange.findWebElement().click();
    }

    public void setWait() {
        try {
            wait.wait(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
