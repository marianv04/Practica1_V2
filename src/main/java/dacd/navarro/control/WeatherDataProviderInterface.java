package dacd.navarro.control;

import dacd.navarro.model.Location;
import dacd.navarro.model.WeatherData;

import java.io.IOException;
import java.util.List;

public interface WeatherDataProviderInterface {
    List<WeatherData> getWeatherData(List<Location> locationObjectList, String apiKey) throws IOException;
}