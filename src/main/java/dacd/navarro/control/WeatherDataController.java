package dacd.navarro.control;

import dacd.navarro.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WeatherDataController {
    public static void execute() throws IOException {
        List<WeatherData> weatherDataObjectsList;

        WeatherApiConnector apiConnector = new WeatherApiConnector();
        WeatherDataProviderInterface dataProvider = new WeatherDataProvider(apiConnector);
        WeatherDataStoreManagerInterface dataStore = new WeatherDataStoreManager(new WeatherDatabaseOperations());

        List<Location> locationObjectList = new ArrayList<>();

        BufferedReader buffer = new BufferedReader(new FileReader("src/main/resources/Locations.csv"));
        String line1;

        StringBuilder content = new StringBuilder();
        BufferedReader buffer2 = new BufferedReader(new FileReader("src/main/resources/ApiKey.txt"));
        String line2;
        while ((line2 = buffer2.readLine()) != null) {
            content.append(line2);
        }

        buffer2.close();
        String apiKey = content.toString();

        while ((line1 = buffer.readLine()) != null) {
            String[] parts = line1.split(",");
            String name = parts[0];
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);

            Location locationObject = new Location(name, latitude, longitude);
            locationObjectList.add(locationObject);

        }

        weatherDataObjectsList = dataProvider.getWeatherData(locationObjectList, apiKey);


        for(int i = 0; i < weatherDataObjectsList.size(); i++){
            dataStore.storeData(weatherDataObjectsList.get(i).getName(), weatherDataObjectsList.get(i).getLocation(), weatherDataObjectsList.get(i).getDate(), weatherDataObjectsList.get(i).getTemperature(), weatherDataObjectsList.get(i).getPrecipitation(), weatherDataObjectsList.get(i).getHumidity(), weatherDataObjectsList.get(i).getClouds(), weatherDataObjectsList.get(i).getWindSpeed(), weatherDataObjectsList.get(i).getDescription());
        }

        buffer.close();
    }
}

