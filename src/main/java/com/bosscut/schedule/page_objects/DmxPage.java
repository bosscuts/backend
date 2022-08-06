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

public class DmxPage {

    private final Query acceptCookiesPopup = new Query().defaultLocator(By.xpath("//*[.='I agree']"));
    private final Query searchBar = new Query().defaultLocator(By.name("q"));
    private final Query googleSearch = new Query().defaultLocator(By.name("btnK"));
    private final Query imFeelingLucky = new Query().defaultLocator(By.name("btnI"));
    private final Query listProduct = new Query().defaultLocator(By.className("listproduct"));
    private final Query listProductExchange = new Query().defaultLocator(By.className("lstcate"));
    private final Query closePopup = new Query().defaultLocator(By.className("dong"));
    private final Query viewMore = new Query().defaultLocator(By.className("view-more"));
    private final Query viewMoreExchange = new Query().defaultLocator(By.className("viewmore"));
    private final WebDriverWait wait;
    private final RemoteWebDriver driver;
    public DmxPage(String pathDriver) throws Exception {
        driver = DriverBase.getDriver(pathDriver);
        initQueryObjects(this, driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofMillis(100));
    }

    public void clickTivi(){
        listProduct.findWebElement().click();
    }


    public List<WebElement> getListProduct() {
        return listProduct.findWebElement().findElements(By.tagName("li"));
    }

    public List<WebElement> getListProductExchange() {
        return listProductExchange.findWebElement().findElements(By.tagName("li"));
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
