package dacd.navarro.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dacd.navarro.model.Location;
import dacd.navarro.model.Weather;

import java.io.IOException;
import java.util.List;

public interface Provider {
    List<Weather> getWeatherData(List<Location> locationObjectList) throws IOException;
    List<Weather> parseWeatherList(Location location, JsonArray weatherList, JsonObject city, String locationName);
    Weather createWeatherObject(Location location, JsonObject weatherData, String locationName, String cityName);
    Gson createGson();
    String serializeWeatherObject(Weather weatherObject);
}
