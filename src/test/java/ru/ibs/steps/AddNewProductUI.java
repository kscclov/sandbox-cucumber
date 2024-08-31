package ru.ibs.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.ibs.steps.baseTest.BaseSteps;

import java.time.Duration;

public class AddNewProductUI {



    @И("Нажатие на кнопку Добавить")
    public void clickOnAddBtn() {
        WebElement bntAdd = BaseSteps.getDriver().findElement(By.xpath("//button[contains(.,'Добавить')]"));
        bntAdd.click();
    }

    @И("Модальное окно с добавлением товара открыто")
    public void VisibleModalWindow() {
        WebDriverWait wait = new WebDriverWait(BaseSteps.getDriver(), Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(., 'Добавление товара')]")));
        } catch (TimeoutException e) {
            Assertions.fail("Ошибка открытия модального окна");
        }
    }

    @И("Ввод значения Наименование:{string}")
    public void fillProductName(String productName) {
        BaseSteps.getDriver().findElement(By.id("name")).sendKeys(productName);
        String enteredValue = BaseSteps.getDriver().findElement(By.id("name")).getAttribute("value");
        Assertions.assertEquals(enteredValue, productName,  "Введенное значение не соответствует ожидаемому названию продукта");
    }

    @И("Выбор типа продукта:{string}")
    public void fillProductType(String productType) {
        WebElement typeDropdown = BaseSteps.getDriver().findElement(By.id("type"));
        typeDropdown.click();
        WebElement fruitOption = new WebDriverWait(BaseSteps.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"type\"]/option[contains(.,'" + productType + "')]")));
        Assertions.assertNotNull(fruitOption, "Вариант с выбором типа продукта не найден");
        fruitOption.click();
    }

    @И("Выбор экзотичности {string}")
    public void fillExotic(String exoticCheckboxStr) {
        WebElement exoticCheckboxElement = BaseSteps.getDriver().findElement(By.id("exotic"));
        Assertions.assertNotNull(exoticCheckboxElement, "Чекбокс жкзотический не найден");
        if (!exoticCheckboxElement.isSelected() && exoticCheckboxStr.equals("true")) {
            exoticCheckboxElement.click();
        }
    }

    @И("Нажатие на кнопку сохранить")
    public void saveProduct(){
        BaseSteps.getDriver().findElement(By.id("save")).click();
    }


    @И("Проверка отображения добавленного продукта {string}, {string}, {string}, {string}")
    public void validateProductParameters(String idCurrentWebElemStr, String expectedName, String expectedType, String expectedExoticStr) {
        int idCurrentWebElem = Integer.parseInt(idCurrentWebElemStr);
        Assertions.assertEquals(BaseSteps.findPreviousProductId(), idCurrentWebElem , "Некорректный ID");

        String productName = BaseSteps.getDriver().findElement(By.xpath("(//tr)[last()]/td[1]")).getText();
        Assertions.assertEquals(expectedName, productName, "Наименование продукта некорректно");

        String productType = BaseSteps.getDriver().findElement(By.xpath("(//tr)[last()]/td[2]")).getText();
        Assertions.assertEquals(expectedType, productType, "Тип продукта некорретен");

        boolean expectedExotic = Boolean.parseBoolean(expectedExoticStr);
        boolean exoticCheckboxBool = false;
        try {
            exoticCheckboxBool = Boolean.parseBoolean(BaseSteps.getDriver().findElement(
                    By.xpath("(//tr)[last()]/td[3]")).getText());
        } catch (Exception e) {
            Assertions.fail("Ошибка при парсинге чекбокса");
        }

        Assertions.assertEquals(expectedExotic, exoticCheckboxBool, "Значение чекбокса некорректное");
    }
}
