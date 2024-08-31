package ru.ibs.steps;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import ru.ibs.steps.baseTest.BaseSteps;

import java.net.MalformedURLException;
import java.net.*;
import java.time.Duration;
import java.util.*;

public class AddNewProductUI {
    public static WebDriver driver = setupRemoteDriver();

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

    @И("Нажатие на кнопку Добавить")
    public void clickOnAddBtn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement bntAdd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Добавить')]")));
        bntAdd.click();
    }

    @И("Модальное окно с добавлением товара открыто")
    public void VisibleModalWindow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(., 'Добавление товара')]")));
        } catch (TimeoutException e) {
            Assertions.fail("Ошибка открытия модального окна");
        }
    }

    @И("Ввод значения Наименование:{string}")
    public void fillProductName(String productName) {
        driver.findElement(By.id("name")).sendKeys(productName);
        String enteredValue = driver.findElement(By.id("name")).getAttribute("value");
        Assertions.assertEquals(enteredValue, productName,  "Введенное значение не соответствует ожидаемому названию продукта");
    }

    @И("Выбор типа продукта:{string}")
    public void fillProductType(String productType) {
        WebElement typeDropdown = driver.findElement(By.id("type"));
        typeDropdown.click();
        WebElement fruitOption = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"type\"]/option[contains(.,'" + productType + "')]")));
        Assertions.assertNotNull(fruitOption, "Вариант с выбором типа продукта не найден");
        fruitOption.click();
    }

    @И("Выбор экзотичности {string}")
    public void fillExotic(String exoticCheckboxStr) {
        WebElement exoticCheckboxElement = driver.findElement(By.id("exotic"));
        Assertions.assertNotNull(exoticCheckboxElement, "Чекбокс жкзотический не найден");
        if (!exoticCheckboxElement.isSelected() && exoticCheckboxStr.equals("true")) {
            exoticCheckboxElement.click();
        }
    }

    @И("Нажатие на кнопку сохранить")
    public void saveProduct(){
        driver.findElement(By.id("save")).click();
    }


    @И("Проверка отображения добавленного продукта {string}, {string}, {string}, {string}")
    public void validateProductParameters(String idCurrentWebElemStr, String expectedName, String expectedType, String expectedExoticStr) {
        int idCurrentWebElem = Integer.parseInt(idCurrentWebElemStr);
        Assertions.assertEquals(BaseSteps.findPreviousProductId(), idCurrentWebElem , "Некорректный ID");

        String productName = driver.findElement(By.xpath("(//tr)[last()]/td[1]")).getText();
        Assertions.assertEquals(expectedName, productName, "Наименование продукта некорректно");

        String productType = driver.findElement(By.xpath("(//tr)[last()]/td[2]")).getText();
        Assertions.assertEquals(expectedType, productType, "Тип продукта некорретен");

        boolean expectedExotic = Boolean.parseBoolean(expectedExoticStr);
        boolean exoticCheckboxBool = false;
        try {
            exoticCheckboxBool = Boolean.parseBoolean(driver.findElement(
                    By.xpath("(//tr)[last()]/td[3]")).getText());
        } catch (Exception e) {
            Assertions.fail("Ошибка при парсинге чекбокса");
        }

        Assertions.assertEquals(expectedExotic, exoticCheckboxBool, "Значение чекбокса некорректное");
    }
}
