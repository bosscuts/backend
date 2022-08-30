package com.bosscut.schedule;

import com.bosscut.schedule.config.DriverFactory;
import com.bosscut.util.OsUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriverBase {

    private static final List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<>());
    private static ThreadLocal<DriverFactory> driverFactoryThread;

    public static void instantiateDriverObject() {
        driverFactoryThread = ThreadLocal.withInitial(() -> {
            DriverFactory driverFactory = new DriverFactory();
            webDriverThreadPool.add(driverFactory);
            return driverFactory;
        });
    }

    public static RemoteWebDriver getDriver(String pathDriver) {
        String osName = OsUtils.getOsName();
        String linkDriver;
        if (osName.startsWith("Windows")) {
            linkDriver = pathDriver + "/windows/chromedriver.exe";
        } else if (osName.startsWith("Linux")) {
            linkDriver = pathDriver + "/linux/chromedriver";
        } else {
            linkDriver = pathDriver + "/mac/chromedriver";
        }
        System.setProperty("webdriver.chrome.driver", linkDriver);
        try {
            return driverFactoryThread.get().getDriver();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearCookies() {
        try {
            driverFactoryThread.get().getStoredDriver().manage().deleteAllCookies();
        } catch (Exception ignored) {
        }
    }

    public static void closeDriverObjects() {
        for (DriverFactory driverFactory : webDriverThreadPool) {
            driverFactory.quitDriver();
        }
    }
}
