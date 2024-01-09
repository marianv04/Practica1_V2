package dacd.navarro.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.navarro.model.Hotel;

import javax.jms.JMSException;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HotelEventsProcessor {
    public static List<String> readHotelEventsDatalake(String hotelDataLakePath) {
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

    public static List<String> getHotelEventsFromBroker(String topicNameHotel, String subscriberName) {
        List<String> responsesList;
        try (AMQTopicHotelSubscriber topicSubscriber = new AMQTopicHotelSubscriber(topicNameHotel, subscriberName)) {
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

    public static Hotel parseHotelEvent(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String island = jsonObject.get("island").getAsString();
        double price = 0.0;
        JsonElement priceElement = jsonObject.get("price");
        if (priceElement != null && !priceElement.isJsonNull()) {
            price = priceElement.getAsDouble();
        }
        int stars = 0;
        JsonElement starsElement = jsonObject.get("stars");
        if (starsElement != null && !starsElement.isJsonNull()) {
            stars = starsElement.getAsInt();
        }
        double scores = 0.0;
        JsonElement scoresElement = jsonObject.get("score");
        if (scoresElement != null && !scoresElement.isJsonNull()) {
            scores = scoresElement.getAsDouble();
        }
        double latitude = jsonObject.get("latitude").getAsDouble();
        double longitude = jsonObject.get("longitude").getAsDouble();
        Hotel hotelObject = new Hotel(island, id, name, price, stars, scores, latitude, longitude);
        return hotelObject;
    }

    public static List<Hotel> saveHotelInDatamart(String path) {
        String topicNameHotel = "search.Hotel";
        String subscriberName = "hotel-provider-BusinessUnit";
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String hotelDatalakePath = path + "datalake/eventstore/search.Hotel/hotel-provider/" + formattedDate + ".events";

        System.out.println("------------------");
        System.out.println("Processing hotel events. Please wait a little bit.");

        List<Hotel> hotelObjectList = new ArrayList<>(readHotelEventsDatalake(hotelDatalakePath).stream()
                .map(HotelEventsProcessor::parseHotelEvent)
                .collect(Collectors.toList()));

        List<Hotel> responsesList = getHotelEventsFromBroker(topicNameHotel, subscriberName).stream()
                .map(HotelEventsProcessor::parseHotelEvent)
                .collect(Collectors.toList());

        hotelObjectList = responsesList.isEmpty() ? hotelObjectList : responsesList;

        System.out.println("Events read, parsed, and added.");
        System.out.println("Processed " + hotelObjectList.size() + " hotel events from " +
                (responsesList.isEmpty() ? "datalake." : "broker."));

        return hotelObjectList;
    }


}
