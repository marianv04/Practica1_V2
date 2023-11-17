package dacd.navarro.control;

import java.sql.*;

public class WeatherDatabaseOperations {
    public static void createTables(Connection connection, String name) throws SQLException {
        Statement statement = connection.createStatement();

        String tableName = "weather_table_" + name;
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "cityName TEXT, " +
                "date TEXT, " +
                "temperature DOUBLE, " +
                "precipitation DOUBLE, " +
                "humidity INT, " +
                "clouds INT, " +
                "wind_speed DOUBLE," +
                "description TEXT" +
                ")";
        statement.execute(createTableSQL);

        statement.close();
        System.out.println(tableName + " created or already exists.");
    }

    public static void insertOrUpdateData(Connection connection, String name, String cityName, String date, double temperature, double precipitation, int humidity, int clouds, double wind_speed, String description) throws SQLException {

        String tableName = "weather_table_" + name;

        String checkQuery = "SELECT * FROM " + tableName + " WHERE date = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, date);
        ResultSet resultSet = checkStatement.executeQuery();
        if (resultSet.next()) {

            String updateQuery = "UPDATE " + tableName + " SET cityName = ?, temperature = ?, precipitation = ?, humidity = ?, clouds = ?, wind_speed = ?, description = ? WHERE date = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, cityName);
            updateStatement.setDouble(2, temperature);
            updateStatement.setDouble(3, precipitation);
            updateStatement.setInt(4, humidity);
            updateStatement.setInt(5, clouds);
            updateStatement.setDouble(6, wind_speed);
            updateStatement.setString(7, description);
            updateStatement.setString(8, date);

            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Data updated in " + tableName);
            }

            updateStatement.close();
        } else {

            String insertQuery = "INSERT INTO " + tableName + " (cityName, date, temperature, precipitation, humidity, clouds, wind_speed, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, date);
            preparedStatement.setDouble(3, temperature);
            preparedStatement.setDouble(4, precipitation);
            preparedStatement.setInt(5, humidity);
            preparedStatement.setInt(6, clouds);
            preparedStatement.setDouble(7, wind_speed);
            preparedStatement.setString(8, description);

            int rowInserted = preparedStatement.executeUpdate();

            if (rowInserted > 0) {
                System.out.println("Data inserted into " + tableName);
            }

            preparedStatement.close();

        }

        checkStatement.close();
    }
}
