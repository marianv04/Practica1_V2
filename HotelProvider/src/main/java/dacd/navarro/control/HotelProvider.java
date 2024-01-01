package dacd.navarro.control;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dacd.navarro.model.Hotel;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class HotelProvider implements Provider{
    private Gson gson = createGson();
    public List<String> getHotelInfo(String jsonResponse, String island) {

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        List<Hotel> hotelObjectList = new ArrayList<>();
        List<String> hotelObjectSerializedList = new ArrayList<>();


        JsonArray propertiesArray = jsonObject.getAsJsonArray("properties");


        for (int i = 0; i < propertiesArray.size(); i++) {
            JsonObject propertyObject = propertiesArray.get(i).getAsJsonObject();

            String id = getString(propertyObject, "id");
            String name = getString(propertyObject, "name");
            double amount = getDouble(propertyObject, "price", "lead", "amount");
            int star = getInt(propertyObject, "star");
            double score = getDouble(propertyObject, "reviews", "score");
            String themeTemp = getString(propertyObject, "offerBadge", "primary", "theme_temp");
            double latitude = getDouble(propertyObject, "mapMarker", "latLong", "latitude");
            double longitude = getDouble(propertyObject, "mapMarker", "latLong", "longitude");

            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Amount: " + amount);
            System.out.println("Star: " + star);
            System.out.println("Score: " + score);
            System.out.println("Theme Temp: " + themeTemp);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.println("-------------------------");

            Instant ts = Instant.now();
            String ss = "hotel-provider";

            Hotel hotel = new Hotel(ts, ss, island, id, name, amount, star, score, themeTemp,latitude, longitude);
            hotelObjectList.add(hotel);
            System.out.println("Objeto creado");
            String hotelJson = serializeHotelObject(hotel);
            hotelObjectSerializedList.add(hotelJson);

        }
        return hotelObjectSerializedList;
    }

    public String getString(JsonObject jsonObject, String... keys) {
        JsonElement element = jsonObject;
        for (String key : keys) {
            if (element != null && element.isJsonObject() && element.getAsJsonObject().has(key)) {
                element = element.getAsJsonObject().get(key);
            } else {
                return "";
            }
        }
        return (element != null && !element.isJsonNull() && element.isJsonPrimitive()) ? element.getAsString() : "";
    }

    public Double getDouble(JsonObject jsonObject, String... keys) {
        JsonElement element = jsonObject;
        for (String key : keys) {
            if (element != null && element.isJsonObject() && element.getAsJsonObject().has(key)) {
                element = element.getAsJsonObject().get(key);
            } else {
                return 0.0;
            }
        }
        return (element != null && !element.isJsonNull() && element.isJsonPrimitive()) ? element.getAsDouble() : 0.0;
    }

    public Integer getInt(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull() && element.isJsonPrimitive()) ? element.getAsInt() : 0;
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

    public String serializeHotelObject(Hotel hotel) {
        return gson.toJson(hotel);
    }

}
