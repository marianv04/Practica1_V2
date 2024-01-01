package dacd.navarro.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dacd.navarro.model.Hotel;

import java.util.List;

public interface Provider {
    List<String> getHotelInfo(String jsonResponse, String island);
    String getString(JsonObject jsonObject, String... keys);
    Double getDouble(JsonObject jsonObject, String... keys);
    Integer getInt(JsonObject jsonObject, String key);
    Gson createGson();
    String serializeHotelObject(Hotel hotel);
}
