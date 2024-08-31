package ru.ibs.steps.baseTest;

import io.cucumber.java.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.util.*;

public class BaseSteps {
    private static WebDriver driver;
    private static Properties props = new Properties();
    private static int idPreviousProduct;
    @BeforeAll
    public static void setup() {
        try (FileInputStream input = new FileInputStream("test/resources/application.properties")) {
            props.load(input);
            String driverType = props.getProperty("type.driver");
            String selenoidUrl = props.getProperty("selenoid.url");

            if("remote".equalsIgnoreCase(driverType)) {
                initRemoteDriver(selenoidUrl);
            } else {

                driver = new ChromeDriver();
                System.setProperty("webdriver.chromedriver.driver",
                        "\\src\\test\\resources\\chromedriver");
                driver.manage().window().maximize();
                driver.get("http://149.154.71.152:8080/food");
            }
        } catch (Exception e){

        }
    }
    public static void initRemoteDriver(String selenoidUrl){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("109.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);
    }
    public static WebDriver getDriver() {
        return driver;
    }

    public static int findPreviousProductId() {
        try {
            Thread.sleep(500);
            idPreviousProduct = Integer.parseInt(driver.findElement(By.xpath(
                    "(//tr)[last()]/th")).getText());
            return idPreviousProduct;
        } catch (Exception e) {
            Assertions.fail("Invalid id value - cannot be converted to int.");
            return 0;
        }
    }

    public static int getIdPreviousProduct(){
        return idPreviousProduct;
    }

    @AfterAll
    public static void endTesting() {
        driver.quit();
    }
}
