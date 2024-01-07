package dacd.navarro.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.navarro.model.Weather;

import javax.jms.JMSException;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class WeatherEventsProcessor {
    public static List<String> readWeatherEventsDatalake(String weatherDataLakePath) {
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

    public static List<String> getWeatherEventsFromBroker(String topicNameWeather, String subscriberName) throws JMSException {
        List<String> responsesList;

        try (AMQTopicWeatherSubscriber topicSubscriber = new AMQTopicWeatherSubscriber(topicNameWeather, subscriberName)) {
            CountDownLatch latch = new CountDownLatch(1);

            topicSubscriber.subscribeToTopic();
            responsesList = topicSubscriber.getEventsList();

            if (!latch.await(15, TimeUnit.SECONDS)) {
                System.out.println(" ");
            }
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return responsesList;
    }

    public static Weather parseWeatherEvent(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        String predictionTime = jsonObject.get("predictionTime").getAsString();
        double temperature = jsonObject.get("temperature").getAsDouble();
        int humidity = jsonObject.get("humidity").getAsInt();
        int clouds = jsonObject.get("clouds").getAsInt();
        double windSpeed = jsonObject.get("windSpeed").getAsDouble();
        double precipitation = jsonObject.get("precipitation").getAsDouble();
        JsonObject locationObject = jsonObject.getAsJsonObject("location");
        String island = locationObject.get("name").getAsString();

        Weather weatherObject = new Weather(island, predictionTime, temperature, precipitation, humidity, clouds, windSpeed);
        return weatherObject;
    }

    public static List<Weather> saveWeatherInDatamart(String path) throws JMSException {
        String topicNameWeather = "prediction.Weather";
        String subscriberName = "prediction-provider-BusinessUnit";

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String weatherDatalakePath = path + "datalake/eventstore/prediction.Weather/prediction-provider/" + formattedDate + ".events";

        System.out.println("----------------");
        System.out.println("Processing weather events. Please wait a little bit.");

        List<Weather> weatherObjectList = new ArrayList<>(readWeatherEventsDatalake(weatherDatalakePath).stream()
                .map(WeatherEventsProcessor::parseWeatherEvent)
                .collect(Collectors.toList()));

        List<Weather> responsesList = getWeatherEventsFromBroker(topicNameWeather, subscriberName).stream()
                .map(WeatherEventsProcessor::parseWeatherEvent)
                .collect(Collectors.toList());

        weatherObjectList = responsesList.isEmpty() ? weatherObjectList : responsesList;

        System.out.println("Events read, parsed, and added.");
        System.out.println("Processed " + weatherObjectList.size() + " weather events from " +
                (responsesList.isEmpty() ? "datalake." : "broker."));

        return weatherObjectList;
    }


}
