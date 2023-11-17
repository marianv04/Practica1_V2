package dacd.navarro.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import dacd.navarro.model.Location;
import dacd.navarro.model.WeatherData;

public class WeatherDataProvider implements WeatherDataProviderInterface {
    private WeatherApiConnector apiConnector;

    public WeatherDataProvider(WeatherApiConnector apiConnector) {
        this.apiConnector = apiConnector;
    }

    public List<WeatherData> getWeatherData(List<Location> locationObjectList, String apiKey) throws IOException {
        List<WeatherData> weatherDataObjectsList = new ArrayList<>();

        for(int i = 0; i < locationObjectList.size(); i++) {
            Location location = locationObjectList.get(i);

            String name = location.getName();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();


            String responseBody = apiConnector.getApiResponse(latitude, longitude, apiKey);

            if (responseBody != null) {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject city = jsonObject.getAsJsonObject("city");
                JsonArray weatherList = jsonObject.getAsJsonArray("list");

                for (JsonElement element : weatherList) {
                    JsonObject weatherData = element.getAsJsonObject();
                    String date = weatherData.get("dt_txt").getAsString();

                    if (date.contains("12:00:00")) {

                        String cityName = city.get("name").getAsString();
                        JsonObject main = weatherData.getAsJsonObject("main");
                        double temperature = main.get("temp").getAsDouble();
                        double precipitation = weatherData.get("pop").getAsDouble();
                        int humidity = main.get("humidity").getAsInt();
                        JsonObject clouds = weatherData.getAsJsonObject("clouds");
                        int cloudiness = clouds.get("all").getAsInt();
                        JsonObject wind = weatherData.getAsJsonObject("wind");
                        double windSpeed = wind.get("speed").getAsDouble();
                        JsonArray weatherArray = weatherData.getAsJsonArray("weather");
                        JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
                        String weatherDescription = weatherObject.get("description").getAsString();

                        WeatherData weatherDataObject = new WeatherData(name, cityName, date, temperature, precipitation, humidity, cloudiness, windSpeed, weatherDescription);
                        weatherDataObjectsList.add(weatherDataObject);
                    }
                }
            }
        }
        return weatherDataObjectsList;
    }
}
