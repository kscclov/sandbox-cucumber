package ru.ibs.steps.baseTest;

import io.cucumber.java.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.*;
import java.util.*;

public class BaseSteps {

    private static int idPreviousProduct;
    public static WebDriver driver = setupRemoteDriver();

    @BeforeAll
    public static RemoteWebDriver setupRemoteDriver() {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("browserName", "chrome");
        selenoidOptions.put("browserVersion", "109.0");
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        capabilities.setCapability("selenoid:options", selenoidOptions);
        try{
            return new RemoteWebDriver(URI.create("http://149.154.71.152:4444/wd/hub").toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int findPreviousProductId() {
        try {
            Thread.sleep(500);
            idPreviousProduct = Integer.parseInt(driver.findElement(By.xpath(
                    "(//tr)[last()]/th")).getText());
            return idPreviousProduct;
        } catch (Exception e) {
            Assertions.fail("Неправильное значение - невозможно преобразовать в int");
            return 0;
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @AfterAll
    public static void quitDriver() {
        driver.quit();
    }
}
