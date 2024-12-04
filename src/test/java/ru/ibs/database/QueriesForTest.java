package ru.ibs.database;

public class QueriesForTest {

    public static final String INSERT =
            "INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?)";
    public static final String SELECT =
            "SELECT * FROM food WHERE food_name = ?";

    public static final String SELECT_LAST_ROW =
    "SELECT * FROM food ORDER BY food_id DESC LIMIT 1";

    public static final String DELETE =
            "DELETE FROM food WHERE food_name = ?";

}

