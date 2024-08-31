package ru.ibs.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.ibs.database.ConnectDB;
import ru.ibs.database.ProductInfoQuery;
import ru.ibs.database.QueriesForTest;

import java.sql.*;

public class AddNewProductDB {
    Connection conn = null;
    @И("Подключение к базе данных h2")
    public Connection connectToDatabase() {
        try {
            conn = DriverManager.getConnection(ConnectDB.DB_URL, ConnectDB.DB_USER, ConnectDB.DB_PASSWORD);
        } catch (SQLException e) {
            Assertions.fail("Ошибка соединения с базой данных:" + e.getMessage());
        }
        return conn;
    }

    @И("Добавление товара в h2db")
    public void addProductToDatabase() {
        try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.INSERT)) { // Добавьте connection в скобки
            pstmt.setString(1, ProductInfoQuery.PRODUCT_NAME);
            pstmt.setString(2, ProductInfoQuery.PRODUCT_TYPE);
            pstmt.setInt(3, ProductInfoQuery.IS_EXOTIC);
            int rowsAffected = pstmt.executeUpdate();
            Assertions.assertEquals(1, rowsAffected, "Количество затронутых строк должно быть = 1");
        } catch (SQLException e) {
            Assertions.fail("Вставка в таблицу не произошла: " + e.getMessage());
        }
    }



    @И("Проверка корректного добавления товара в бд")
    public void verifyProductInDatabase() {
        try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.SELECT_LAST_ROW)) {
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                Assertions.assertEquals(ProductInfoQuery.PRODUCT_NAME, result.getString("food_name"),
                        "Наименование продукта не соответствует ожидаемому.");
                Assertions.assertEquals(ProductInfoQuery.PRODUCT_TYPE, result.getString("food_type"),
                        "Тип продукта не соответствует ожидаемому.");
                Assertions.assertEquals(ProductInfoQuery.IS_EXOTIC, result.getInt("food_exotic"),
                        "Экзотичность продукта не соответствует ожидаемому.");
            } else {
                Assertions.fail("В таблице нет добавленных продуктов");
            }
        } catch (SQLException e) {
            Assertions.fail("Операция SELECT не удалась:" + e.getMessage());
        }
    }


    @И("Удаление товара из БД")
    public void deleteProductFromDatabase() {
        try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.DELETE)) {
            pstmt.setString(1, ProductInfoQuery.PRODUCT_NAME);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Assertions.fail("Не удалось выполнить запрос на удаление:" + e.getMessage());
        }
    }

    @И("Проверка удаления товара из БД")
    public void verifyProductDeletion() {
        try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.SELECT)) {
            pstmt.setString(1, ProductInfoQuery.PRODUCT_NAME);
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                Assertions.fail("Ошибка удаления тестового значения");
            }
        } catch (SQLException e) {
            Assertions.fail("Операция SELECT не удалась:" + e.getMessage());
        }
    }
}
