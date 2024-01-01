package dacd.navarro.control;

import com.google.gson.*;
import dacd.navarro.model.Hotel;
import dacd.navarro.model.Location;
import dacd.navarro.model.Weather;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Datamart {
    public static List<String> readHotelEvents(String hotelDataLakePath) {
        List<String> hotelEvents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(hotelDataLakePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String hotelEvent = line;
                if (hotelEvent != null) {
                    hotelEvents.add(hotelEvent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hotelEvents;
    }

    public static List<String> readWeatherEvents(String weatherDataLakePath) {
        List<String> weatherEvents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(weatherDataLakePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String weatherEvent = line;
                if (weatherEvent != null) {
                    weatherEvents.add(weatherEvent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherEvents;
    }

    public static List<String> getHotelEventsFromBroker() throws JMSException {
        String subscriberNameHotel = "hotel-provider";
        String topicNameHotel = "search.Hotel";

        Subscriber topicSubscriberHotel = new AMQTopicHotelSubscriber(subscriberNameHotel, topicNameHotel);
        topicSubscriberHotel.subscribeToTopic();
        topicSubscriberHotel.close();
        List<String> hotelEventsList = topicSubscriberHotel.getEventsList();
        return hotelEventsList;
    }

    public static List<String> getWeatherEventsFromBroker() throws JMSException {
        String subscriberNameWeather = "prediction-provider";
        String topicNameWeather = "prediction.Weather";

        Subscriber topicSubscriberWeather = new AMQTopicWeatherSubscriber(subscriberNameWeather, topicNameWeather);
        topicSubscriberWeather.subscribeToTopic();
        topicSubscriberWeather.close();
        List<String> weatherEventsList = topicSubscriberWeather.getEventsList();
        return weatherEventsList;
    }

    public static Hotel parseHotelEvent(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        String tsString = jsonObject.get("ts").getAsString();
        String ss = jsonObject.get("ss").getAsString();
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String island = jsonObject.get("island").getAsString();
        double price = 0.0;
        JsonElement priceElement = jsonObject.get("price");
        if (priceElement != null && !priceElement.isJsonNull()) {
            price = priceElement.getAsDouble();
        }

        int stars = 0;  // Valor predeterminado
        JsonElement starsElement = jsonObject.get("stars");
        if (starsElement != null && !starsElement.isJsonNull()) {
            stars = starsElement.getAsInt();
        }

        double scores = 0.0;  // Valor predeterminado
        JsonElement scoresElement = jsonObject.get("score");
        if (scoresElement != null && !scoresElement.isJsonNull()) {
            scores = scoresElement.getAsDouble();
        }
        String themeTemp = jsonObject.get("themeTemp").getAsString();
        double latitude = jsonObject.get("latitude").getAsDouble();
        double longitude = jsonObject.get("longitude").getAsDouble();

        Instant ts = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(tsString));


        System.out.println("ts: " + ts);
        System.out.println("ss: " + ss);
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Island: " + island);
        System.out.println("Price: " + price);
        System.out.println("Stars: " + stars);
        System.out.println("Scores: " + scores);
        System.out.println("Theme Temp: " + themeTemp);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);

        Hotel hotelObject = new Hotel(ts, ss, island, id, name, price, stars, scores, themeTemp, latitude, longitude);
        return hotelObject;
    }

    public static Weather parseWeatherEvent(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        String tsString = jsonObject.get("ts").getAsString();
        String ss = jsonObject.get("ss").getAsString();
        String predictionTime = jsonObject.get("predictionTime").getAsString();
        double temperature = jsonObject.get("temperature").getAsDouble();
        int humidity = jsonObject.get("humidity").getAsInt();
        int clouds = jsonObject.get("clouds").getAsInt();
        double windSpeed = jsonObject.get("windSpeed").getAsDouble();
        double precipitation = jsonObject.get("precipitation").getAsDouble();
        JsonObject locationObject = jsonObject.getAsJsonObject("location");
        String island = locationObject.get("name").getAsString();
        double latitude = locationObject.get("latitude").getAsDouble();
        double longitude = locationObject.get("longitude").getAsDouble();
        Instant ts = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(tsString));
        System.out.println("ts: " + ts);
        System.out.println("ss: " + ss);
        System.out.println("Prediction Time: " + predictionTime);
        System.out.println("Temperature: " + temperature);
        System.out.println("Humidity: " + humidity);
        System.out.println("Clouds: " + clouds);
        System.out.println("Wind Speed: " + windSpeed);
        System.out.println("Precipitation: " + precipitation);
        System.out.println("Location: " + island + " (" + latitude + ", " + longitude + ")");

        Location location = new Location(island, latitude, longitude);
        Weather weatherObject = new Weather(location, ts, ss, predictionTime, temperature, precipitation, humidity, clouds, windSpeed);
        return weatherObject;
    }

    public static Map<String, List<Weather>> saveWeatherInDatamart() throws JMSException {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Map<String, List<Weather>> weatherListsMap = new HashMap<>();

        List<Weather> weatherObjectList = weatherListsMap.computeIfAbsent(formattedDate, k -> new ArrayList<>());

        String weatherDatalakePath = "datalake/prediction.Weather/prediction-provider/" + formattedDate + ".events";

        List<String> weatherEventsList = getWeatherEventsFromBroker();
        for (String weatherEvent : weatherEventsList) {
            Weather weather = parseWeatherEvent(weatherEvent);
            weatherObjectList.add(weather);
        }

        if (weatherEventsList.size() == 0) {
            System.out.println("----------------");
            System.out.println("DATALAKE WEATHER");
            List<String> weatherEventsListDatalake = readWeatherEvents(weatherDatalakePath);
            for (String weatherEvent : weatherEventsListDatalake) {
                Weather weather = parseWeatherEvent(weatherEvent);
                weatherObjectList.add(weather);
            }
            System.out.println("----------------");
            System.out.println(weatherObjectList.size());
        }
        return weatherListsMap;

    }
    public static Map<String, List<Hotel>> saveHotelInDatamart() throws JMSException {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Map<String, List<Hotel>> hotelListsMap = new HashMap<>();
        List<Hotel> hotelObjectList = hotelListsMap.computeIfAbsent(formattedDate, k -> new ArrayList<>());

        String hotelDatalakePath = "datalake/search.Hotel/hotel-provider/" + formattedDate + ".events";

        List<String> hotelEventsList = getHotelEventsFromBroker();
        for (String hotelEvent : hotelEventsList) {
            Hotel hotel = parseHotelEvent(hotelEvent);
            hotelObjectList.add(hotel);
        }

        if (hotelEventsList.size() == 0) {
            System.out.println("------------------");
            System.out.println("DATALAKE HOTEL");
            List<String> hotelEventsListDatalake = readHotelEvents(hotelDatalakePath);
            for (String hotelEvent : hotelEventsListDatalake) {
                Hotel hotel = parseHotelEvent(hotelEvent);
                hotelObjectList.add(hotel);
            }
            System.out.println("----------------");
            System.out.println(hotelObjectList.size());
        }
        return hotelListsMap;

    }



}
