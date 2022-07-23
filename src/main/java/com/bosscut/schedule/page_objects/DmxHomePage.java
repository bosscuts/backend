package com.bosscut.schedule.page_objects;

import com.bosscut.schedule.DriverBase;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

public class DmxHomePage {

    private final Query acceptCookiesPopup = new Query().defaultLocator(By.xpath("//*[.='I agree']"));
    private final Query searchBar = new Query().defaultLocator(By.name("q"));
    private final Query googleSearch = new Query().defaultLocator(By.name("btnK"));
    private final Query imFeelingLucky = new Query().defaultLocator(By.name("btnI"));
    private final Query listProduct = new Query().defaultLocator(By.className("listproduct"));
    private final Query viewMore = new Query().defaultLocator(By.className("view-more"));
    private final WebDriverWait wait;
    private final RemoteWebDriver driver;
    public DmxHomePage() throws Exception {
        driver = DriverBase.getDriver();
        initQueryObjects(this, driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofMillis(100));
    }

    public void clickTivi(){
        listProduct.findWebElement().click();
    }


    public List<WebElement> getListProduct() {
        return listProduct.findWebElement().findElements(By.tagName("li"));
    }

    public void viewMore() {
        viewMore.findWebElement().click();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
