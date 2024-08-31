package ru.ibs.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.ibs.database.ConnectDB;
import ru.ibs.database.ProductInfoQuery;
import ru.ibs.database.QueriesForTest;

import java.sql.*;

public class AddNewProductDB {
    @И("Добавить товар в базу данных")
    public void addNewProductTest(){
        try (Connection conn =
                     DriverManager.getConnection(ConnectDB.DB_URL, ConnectDB.DB_USER, ConnectDB.DB_PASSWORD)) {
            try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.INSERT)) {
                pstmt.setInt(1, ProductInfoQuery.PRODUCT_ID);
                pstmt.setString(2, ProductInfoQuery.PRODUCT_NAME);
                pstmt.setString(3, ProductInfoQuery.PRODUCT_TYPE);
                pstmt.setInt(4, ProductInfoQuery.IS_EXOTIC);
                Assertions.assertEquals(1, pstmt.executeUpdate(), "The number of affected rows should be 1.");
            } catch (SQLException e) {
                Assertions.fail("The insertion into the table did not occur: " + e.getMessage());
            }

            try (PreparedStatement pstmt = conn.prepareStatement(QueriesForTest.SELECT)) {
                pstmt.setInt(1, ProductInfoQuery.PRODUCT_ID);
                ResultSet result = pstmt.executeQuery();
                if (result.next()) {
                    Assertions.assertEquals(ProductInfoQuery.PRODUCT_ID, result.getInt("food_id"),
                            "The product ID does not match the expected one.");
                    Assertions.assertEquals(ProductInfoQuery.PRODUCT_NAME, result.getString("food_name"),
                            "The product name does not match the expected one.");
                    Assertions.assertEquals(ProductInfoQuery.PRODUCT_TYPE, result.getString("food_type"),
                            "The product type does not match what is expected.");
                    Assertions.assertEquals(ProductInfoQuery.IS_EXOTIC, result.getInt("food_exotic"),
                            "The value of the exotic checkbox does not match the expected value.");
                }
            } catch (SQLException e) {
                Assertions.fail("The SELECT operation failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            Assertions.fail("The connection to the database did not occur: " + e.getMessage());
        }
    }
}
