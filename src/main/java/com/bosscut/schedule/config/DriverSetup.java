package com.bosscut.schedule.config;

import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface DriverSetup {
    RemoteWebDriver getWebDriverObject(BrowserMobProxy mobProxy, DesiredCapabilities capabilities);
}