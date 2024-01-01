package dacd.navarro.control;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import dacd.navarro.model.*;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {

    public static void execute(String apiKey) throws IOException, JMSException {
        List<Weather> weatherDataObjectsList;

        WeatherApiConnector apiConnector = new WeatherApiConnector();
        Provider dataProvider = new WeatherProvider(apiConnector, apiKey);

        List<Location> locationObjectList = new ArrayList<>();

        try (InputStream inputStream = WeatherController.class.getClassLoader().getResourceAsStream("Locations.csv")) {
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader reader = new CSVReader(inputStreamReader);

                String[] line;
                while ((line = reader.readNext()) != null) {

                    String name = line[0];
                    double latitude = Double.parseDouble(line[1]);
                    double longitude = Double.parseDouble(line[2]);

                    Location locationObject = new Location(name, latitude, longitude);
                    locationObjectList.add(locationObject);
                }

            } else {
                throw new IOException("Locations.csv could not be loaded.");
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        weatherDataObjectsList = dataProvider.getWeatherData(locationObjectList);

        for (int i = 0; i < weatherDataObjectsList.size(); i++) {
            String json = dataProvider.serializeWeatherObject(weatherDataObjectsList.get(i));
            System.out.println(json);
            MessageSender.messageToBroker(json);
        }
    }
}

