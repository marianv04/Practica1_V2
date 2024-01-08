package dacd.navarro.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import java.io.IOException;

public class HotelApiConnector {
    public static int getRegionId(String query, String apiKey) {
        String locale = "es_ES";
        String baseUrl = "https://hotels-com-provider.p.rapidapi.com/v2/regions";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addQueryParameter("query", query)
                .addQueryParameter("domain", "ES")
                .addQueryParameter("locale", locale);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "hotels-com-provider.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("Response Body: " + responseBody);

                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

                JsonArray data = json.getAsJsonArray("data");
                if (data != null && data.size() > 0) {
                    JsonObject firstResult = data.get(0).getAsJsonObject();
                    String gaiaId = firstResult.getAsJsonPrimitive("gaiaId").getAsString();

                    return Integer.parseInt(gaiaId);

                } else {
                    System.out.println("No results found.");
                }
            } else {
                System.out.println("Error in respond, code: " + response.code());
                String responseBody = response.body().string();
                System.out.println("Error Body: " + responseBody);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Waiting time finished.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(null);
    }

    public static String getHotels(int regionId, String apiKey, String checkIn, String checkOut, int adults) {
        String baseUrl = "https://hotels-com-provider.p.rapidapi.com/v2/hotels/search";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addQueryParameter("region_id", String.valueOf(regionId))
                .addQueryParameter("locale", "es_ES")
                .addQueryParameter("checkin_date", checkIn)
                .addQueryParameter("sort_order", "REVIEW")
                .addQueryParameter("adults_number", String.valueOf(adults))
                .addQueryParameter("domain", "ES")
                .addQueryParameter("checkout_date", checkOut)
                .addQueryParameter("lodging_type", "HOTEL");

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "hotels-com-provider.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                return responseBody;
            } else {
                System.out.println("Error in respond: " + response.code());
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Waiting time finished.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
