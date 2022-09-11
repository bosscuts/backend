package com.bosscut;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.InetSocketAddress;

public class test {
    public static void main(String[] args) {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setChainedProxy(new InetSocketAddress("103.176.112.191", 22222));
        proxy.chainedProxyAuthorization("ProxyVN271310", "mvpZX3Jz", AuthType.BASIC);
        proxy.setTrustAllServers(true);
        proxy.start(0);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        System.setProperty("webdriver.chrome.driver", "/home/hoaronal/project/driver/linux/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.addArguments("--ignore-certificate-errors");

        WebDriver driver = new ChromeDriver(options);
        driver.navigate().to("https://www.dienmayxanh.com/");
    }
}
