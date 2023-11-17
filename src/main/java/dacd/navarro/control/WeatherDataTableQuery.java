package dacd.navarro.control;


import javax.swing.*;
import java.sql.*;

public class WeatherDataTableQuery {
    public static void fetchTableData(String islandName, String selectedDate) {
        String jdbcURL = "jdbc:sqlite:WeatherDatabase.db";
        String tableName = "weather_table_" + islandName;
        String sql = "SELECT * FROM " + tableName + " WHERE date = ?";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);

            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedDate);
                ResultSet resultSet = preparedStatement.executeQuery();

                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    // Get data from each row
                    String cityName = resultSet.getString("cityName");
                    String date = resultSet.getString("date");
                    double temp = resultSet.getInt("temperature");
                    double precipitation = resultSet.getDouble("precipitation");
                    int humidity = resultSet.getInt("humidity");
                    int clouds = resultSet.getInt("clouds");
                    double windSpeed = resultSet.getInt("wind_speed");
                    String description = resultSet.getString("description");

                    result.append("City: ").append(cityName).append("\n");
                    result.append("Date: ").append(date).append("\n");
                    result.append("Temperature: ").append(temp).append("\n");
                    result.append("Precipitation: ").append(precipitation).append("\n");
                    result.append("Humidity: ").append(humidity).append("\n");
                    result.append("Cloudiness: ").append(clouds).append("\n");
                    result.append("Wind Speed: ").append(windSpeed).append("\n");
                    result.append("Weather Description: ").append(description).append("\n\n");
                }
                showMessageDialog("Weather Data for " + islandName + " on " + selectedDate, result.toString());
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showMessageDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }
}

