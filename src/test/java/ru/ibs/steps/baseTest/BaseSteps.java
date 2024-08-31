package ru.ibs.steps.baseTest;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseSteps {
    private static WebDriver driver;

    private static int idPreviousProduct;
    @BeforeAll
    public static void setup() {

        driver = new ChromeDriver();
        System.setProperty("webdriver.chromedriver.diver",
                "src/test/resources/chromedriver");
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/food");
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
