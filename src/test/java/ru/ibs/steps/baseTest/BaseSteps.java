package ru.ibs.steps.baseTest;

import io.cucumber.java.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class BaseSteps {
    public static WebDriver driver;
    private static Properties props = new Properties();
    private static int idPreviousProduct;

    @BeforeAll
    public static void runDriver() {
        try (FileInputStream input = new FileInputStream("test/resources/application.properties")) {
            props.load(input);
            String driverType = props.getProperty("type.driver");
            String selenoidUrl = props.getProperty("selenoid.url");

            if ("remote".equalsIgnoreCase(driverType)) {
                initRemoteDriver(selenoidUrl);
            } else {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.get("http://149.154.71.152:8080/food");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Failed to initialize the WebDriver.");
        }
    }

    public static void initRemoteDriver(String selenoidUrl) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("109.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);

        try {
            driver = new RemoteWebDriver(new URL(selenoidUrl), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assertions.fail("Failed to initialize remote WebDriver.");
        }
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            Assertions.fail("WebDriver is not initialized. Please check the setup.");
        }
        return driver;
    }

    public static int findPreviousProductId() {
        try {
            Thread.sleep(500);
            idPreviousProduct = Integer.parseInt(getDriver().findElement(By.xpath("(//tr)[last()]/th")).getText());
            return idPreviousProduct;
        } catch (Exception e) {
            Assertions.fail("Invalid id value - cannot be converted to int.");
            return 0;
        }
    }

    @AfterAll
    public static void endTesting() {
        if (driver != null) {
            driver.quit();
        }
    }
}