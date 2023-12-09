package dacd.navarro.control;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dacd.navarro.model.Location;
import dacd.navarro.model.Weather;

public class WeatherProvider implements WeatherProviderInterface {
    private WeatherApiConnector apiConnector;
    private String apiKey;
    private Gson gson;

    public WeatherProvider(WeatherApiConnector apiConnector, String apiKey) {
        this.apiConnector = apiConnector;
        this.apiKey = apiKey;
        this.gson = createGson();
    }

    public List<Weather> getWeatherData(List<Location> locationObjectList) throws IOException {
        List<Weather> weatherObjectsList = new ArrayList<>();

        for (Location location : locationObjectList) {
            String responseBody = apiConnector.getApiResponse(location.getLatitude(), location.getLongitude(), apiKey);

            if (responseBody != null) {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject city = jsonObject.getAsJsonObject("city");
                JsonArray weatherList = jsonObject.getAsJsonArray("list");

                weatherObjectsList.addAll(parseWeatherList(location, weatherList, city, location.getName()));
            }
        }

        return weatherObjectsList;
    }

    public List<Weather> parseWeatherList(Location location, JsonArray weatherList, JsonObject city, String locationName) {
        List<Weather> weatherObjectsList = new ArrayList<>();

        for (JsonElement element : weatherList) {
            JsonObject weatherData = element.getAsJsonObject();
            String date = weatherData.get("dt_txt").getAsString();

            if (date.contains("12:00:00")) {
                String cityName = city.get("name").getAsString();
                Weather weatherDataObject = createWeatherObject(location, weatherData, locationName, cityName);
                weatherObjectsList.add(weatherDataObject);
            }
        }

        return weatherObjectsList;
    }

    public Weather createWeatherObject(Location location, JsonObject weatherData, String locationName, String cityName) {
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

        Instant ts = Instant.now();
        String ss = "prediction-provider";
        String predictionTime = date;

        return new Weather(location, ts, ss, predictionTime, locationName, cityName, date, temperature, precipitation, humidity, cloudiness, windSpeed, weatherDescription);
    }

    public Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                    @Override
                    public void write(JsonWriter out, Instant ts) throws IOException {
                        if (ts == null) {
                            out.nullValue();
                        } else {
                            out.value(ts.toString());
                        }
                    }

                    @Override
                    public Instant read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        return Instant.parse(in.nextString());
                    }
                })
                .create();
    }

    public String serializeWeatherObject(Weather weatherObject) {
        return gson.toJson(weatherObject);
    }
}
