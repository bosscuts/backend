package com.bosscut.schedule.page_objects;

import com.bosscut.schedule.DriverBase;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

public class DmxPage {

    private final Query listProduct = new Query().defaultLocator(By.className("listproduct"));
    private final Query listProductExchange = new Query().defaultLocator(By.className("lstcate"));
    private final Query listProductExchange1 = new Query().defaultLocator(By.id("o-pro"));
    private final Query closePopup = new Query().defaultLocator(By.className("dong"));
    private final Query openPlace = new Query().defaultLocator(By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1)"));
    private final Query viewMore = new Query().defaultLocator(By.className("view-more"));
    private final Query viewMoreExchange = new Query().defaultLocator(By.className("viewmore"));

    private final Query provinceClick1 = new Query().defaultLocator(
            By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1) > div > div > aside > a:nth-child(3)")
    );
    private final Query provinceClick2 = new Query().defaultLocator(
            By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1) > div > div > aside > a:nth-child(6)")
    );
    private final Query provinceClick3 = new Query().defaultLocator(
            By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1) > div > div > aside > a:nth-child(9)")
    );
    private final Query provinceClick4 = new Query().defaultLocator(
            By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1) > div > div > aside > a:nth-child(62)")
    );

    private final Query provinceClickAll = new Query().defaultLocator(
            By.cssSelector("body > div.wrapmain.notover > div.boxcate > div > aside > ul > li:nth-child(1) > div > div > aside > a:nth-child(1)")
    );

    private final WebDriverWait wait;
    private final RemoteWebDriver driver;

    public DmxPage(String pathDriver) throws Exception {
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
            clickSearchProvince(provinceClick);
            List<WebElement> productElement = listProductExchange.findWebElement().findElements(By.tagName("li"));
            productElement.forEach(product -> {
                WebElement linkProd = product.findElement(By.tagName("a"));
                String productLink = linkProd.getDomAttribute("href");
                productLinks.add("https://www.dienmayxanh.com" + productLink);
            });
        });
        return productLinks;
    }

    public List<WebElement> getListProductExchange1() {
        List<WebElement> cates = listProductExchange1.findWebElement().findElements(By.className("cate"));
        List<WebElement> products = new ArrayList<>();
        cates.forEach(cate -> {
            List<WebElement> productElement = listProductExchange1.findWebElement().findElements(By.tagName("li"));
            products.addAll(productElement);
        });
        return products;
    }

    public List<WebElement> getListMayDoiTra() {
        return listProduct.findWebElement().findElements(By.tagName("li"));
    }

    public void viewMore() {
        viewMore.findWebElement().click();
    }

    public void closePopup() {
        closePopup.findWebElement().click();
    }

    public void clickSearchProvince(Query provinceClick) {
        openPlace.findWebElement().click();
        try {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("getListProductExchange error!");
            }
            provinceClick.findWebElement().click();
            for (int i = 0; i < 1000; i++) {
                TimeUnit.SECONDS.sleep(1);
                viewMoreExchange();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            System.out.println("Error clickSearchProvince!");
        }
    }

    public void viewMoreExchange() {
        viewMoreExchange.findWebElement().click();
    }

    public int getTotalElement() {
        String totalElement = driver.findElement(By.className("remain")).getText();
        return Integer.parseInt(totalElement);
    }

    public int getTotalElementExchange() {
        String totalElement = driver.findElement(By.className("viewmore")).getText();
        return Integer.parseInt(totalElement);
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
