package client.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SeleniumDriverManager {

    private static final Logger LOG = LogManager.getLogger(SeleniumDriverManager.class);
    private static final ThreadLocal<RemoteWebDriver> localDrivers = new ThreadLocal<>();
    private static final List<RemoteWebDriver> drivers = new ArrayList<>();

    private static final String CHROME_DRIVER_NAME = "chromedriver";
    private static final String FIREFOX_DRIVER_NAME = "geckodriver";
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver";

    public static String getFirefoxDriverName() {
        return System.getProperty("os.name").startsWith("Windows") ? FIREFOX_DRIVER_NAME + ".exe" : FIREFOX_DRIVER_NAME;
    }

    public static String getChromeDriverName() {
        return System.getProperty("os.name").startsWith("Windows") ? CHROME_DRIVER_NAME + ".exe" : CHROME_DRIVER_NAME;
    }

    public static void setChromeDriverPath(Path driverPath) {
        System.setProperty(WEBDRIVER_CHROME_DRIVER, driverPath.toString());
    }

    public static void setFirefoxDriverPath(Path driverPath) {
        System.setProperty(WEBDRIVER_GECKO_DRIVER, driverPath.toString());
    }

    public static RemoteWebDriver getChromeDriver() {
        ChromeDriver driver = new ChromeDriver();
        localDrivers.set(driver);
        drivers.add(driver);
        return driver;
    }

    public static RemoteWebDriver getFirefoxDriver() {
        FirefoxDriver driver = new FirefoxDriver();
        localDrivers.set(driver);
        drivers.add(driver);
        return driver;
    }

    public static RemoteWebDriver getCurrentDriver() {
        return localDrivers.get();
    }

    public static void closeAllDrivers() {
        localDrivers.remove();
        for (RemoteWebDriver driver : drivers) {
            try {
                driver.quit();
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
            }
        }
    }
}