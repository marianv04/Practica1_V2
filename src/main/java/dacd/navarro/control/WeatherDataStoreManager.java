package dacd.navarro.control;

import java.sql.*;

public class WeatherDataStoreManager implements WeatherDataStoreManagerInterface {
    private WeatherDatabaseOperations databaseOperations;

    public WeatherDataStoreManager(WeatherDatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
    }
    public void storeData(String name, String cityName, String date, double temperature, double precipitation, int humidity, int clouds, double wind_speed, String description) {
        String jdbcURL = "jdbc:sqlite:WeatherDatabase.db";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL);

            if (connection != null) {

                databaseOperations.createTables(connection, name);

                databaseOperations.insertOrUpdateData(connection, name, cityName, date, temperature, precipitation, humidity, clouds, wind_speed, description);
                System.out.println("---------------------------");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
