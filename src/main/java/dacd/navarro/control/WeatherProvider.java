package dacd.navarro.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import dacd.navarro.model.Location;
import dacd.navarro.model.Weather;

public class WeatherProvider implements WeatherProviderInterface {
    private WeatherApiConnector apiConnector;
    private String apiKey;
    private Gson gson;

    public WeatherProvider(WeatherApiConnector apiConnector, String apiKey) {
        this.apiConnector = apiConnector;
        this.apiKey = apiKey;
    }

    public List<Weather> getWeatherData(List<Location> locationObjectList) throws IOException {
        List<Weather> weatherDataObjectsList = new ArrayList<>();

        for (Location location : locationObjectList) {
            String responseBody = apiConnector.getApiResponse(location.getLatitude(), location.getLongitude(), apiKey);

            if (responseBody != null) {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject city = jsonObject.getAsJsonObject("city");
                JsonArray weatherList = jsonObject.getAsJsonArray("list");

                weatherDataObjectsList.addAll(parseWeatherList(weatherList, city, location.getName()));
            }
        }

        return weatherDataObjectsList;
    }

    public List<Weather> parseWeatherList(JsonArray weatherList, JsonObject city, String locationName) {
        List<Weather> weatherDataObjectsList = new ArrayList<>();

        for (JsonElement element : weatherList) {
            JsonObject weatherData = element.getAsJsonObject();
            String date = weatherData.get("dt_txt").getAsString();

            if (date.contains("12:00:00")) {
                String cityName = city.get("name").getAsString();
                Weather weatherDataObject = createWeatherObject(weatherData, locationName, cityName);
                weatherDataObjectsList.add(weatherDataObject);
            }
        }

        return weatherDataObjectsList;
    }

    public Weather createWeatherObject(JsonObject weatherData, String locationName, String cityName) {
        String date = weatherData.get("dt_txt").getAsString();
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


        return new Weather(locationName, cityName, date, temperature, precipitation, humidity, cloudiness, windSpeed, weatherDescription);
    }

    public String serializeWeatherObject(Weather weatherObject) {
        return gson.toJson(weatherObject);
    }
}
