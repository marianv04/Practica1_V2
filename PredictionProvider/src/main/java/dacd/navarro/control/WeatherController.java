package dacd.navarro.control;

import dacd.navarro.model.*;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {

    public static void execute(String apiKey) throws IOException, JMSException {
        List<Weather> weatherDataObjectsList;

        WeatherApiConnector apiConnector = new WeatherApiConnector();
        WeatherProviderInterface dataProvider = new WeatherProvider(apiConnector, apiKey);

        List<Location> locationObjectList = new ArrayList<>();

        try (InputStream inputStream = WeatherController.class.getClassLoader().getResourceAsStream("Locations.csv")) {
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(inputStreamReader);
                String line;

                while ((line = buffer.readLine()) != null) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    double latitude = Double.parseDouble(parts[1]);
                    double longitude = Double.parseDouble(parts[2]);

                    Location locationObject = new Location(name, latitude, longitude);
                    locationObjectList.add(locationObject);
                }

                buffer.close();
            } else {
                throw new IOException("Locations.csv could not be loaded.");
            }
        }

        weatherDataObjectsList = dataProvider.getWeatherData(locationObjectList);

        for (int i = 0; i < weatherDataObjectsList.size(); i++) {
            String json = dataProvider.serializeWeatherObject(weatherDataObjectsList.get(i));
            System.out.println(json);
            MessageSender.messageToBroker(json);
        }
    }
}

