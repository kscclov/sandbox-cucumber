package ru.ibs.steps.baseTest;

import io.cucumber.java.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import static ru.ibs.steps.AddNewProductUI.driver;

public class BaseSteps {
    private static int idPreviousProduct;

    public static int findPreviousProductId() {
        try {
            Thread.sleep(500);
            idPreviousProduct = Integer.parseInt(driver.findElement(By.xpath("(//tr)[last()]/th")).getText());
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