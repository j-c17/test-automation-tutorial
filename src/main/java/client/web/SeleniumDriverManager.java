package client.web;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.file.Path;

public class SeleniumDriverManager {

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
        return new ChromeDriver();
    }

    public static RemoteWebDriver getFirefoxDriver() {
        return new FirefoxDriver();
    }
}