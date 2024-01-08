package dacd.navarro.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.time.Instant;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.*;
import dacd.navarro.model.*;

public class WeatherProvider implements Provider {
    private WeatherApiConnector apiConnector;
    private String apiKey;
    private Gson gson;

    public WeatherProvider(WeatherApiConnector apiConnector, String apiKey) {
        this.apiConnector = apiConnector;
        this.apiKey = apiKey;
        this.gson = createGson();
    }

    public List<Weather> getWeatherData(List<Location> locationObjectList, List<String> islandsNames) throws IOException {
        List<Weather> weatherDataObjectsList = new ArrayList<>();
        for (int i = 0; i < locationObjectList.size(); i++) {
            Location location = locationObjectList.get(i);
            String island = islandsNames.get(i);
            String responseBody = apiConnector.getApiResponse(location.getLatitude(), location.getLongitude(), apiKey);

            if (responseBody != null) {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray weatherList = jsonObject.getAsJsonArray("list");
                weatherDataObjectsList.addAll(parseWeatherList(location, weatherList, island));
            }
        }
        return weatherDataObjectsList;
    }

    public List<Weather> parseWeatherList(Location location, JsonArray weatherList, String island) {
        List<Weather> weatherDataObjectsList = new ArrayList<>();

        for (JsonElement element : weatherList) {
            JsonObject weatherData = element.getAsJsonObject();
            String date = weatherData.get("dt_txt").getAsString();

            if (date.contains("12:00:00")) {
                Weather weatherDataObject = createWeatherObject(location, weatherData, island);
                weatherDataObjectsList.add(weatherDataObject);
            }
        }

        return weatherDataObjectsList;
    }

    public Weather createWeatherObject(Location location, JsonObject weatherData, String island) {
        String date = weatherData.get("dt_txt").getAsString();
        JsonObject main = weatherData.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        double precipitation = weatherData.get("pop").getAsDouble();
        int humidity = main.get("humidity").getAsInt();
        JsonObject clouds = weatherData.getAsJsonObject("clouds");
        int cloudiness = clouds.get("all").getAsInt();
        JsonObject wind = weatherData.getAsJsonObject("wind");
        double windSpeed = wind.get("speed").getAsDouble();

        Instant ts = Instant.now();
        String ss = "prediction-provider";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp predictionTs = null;
        try {
            Date dateFormatted = formatter.parse(date);
            long miliseconds = dateFormatted.getTime();
            predictionTs = new Timestamp(miliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Weather(location, ts, ss, island, predictionTs, temperature, precipitation, humidity, cloudiness, windSpeed);
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
