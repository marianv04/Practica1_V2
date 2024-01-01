package dacd.navarro.control;

import dacd.navarro.model.Hotel;
import dacd.navarro.model.Weather;

import javax.jms.JMSException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws JMSException {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Map<String, List<Weather>> weatherListsMap;
        Map<String, List<Hotel>> hotelListsMap;
        weatherListsMap = Datamart.saveWeatherInDatamart();
        hotelListsMap = Datamart.saveHotelInDatamart();

        List<Weather> weatherObjects= weatherListsMap.get(formattedDate);

        List<Hotel> hotelObjects= hotelListsMap.get(formattedDate);

        String chosenIsland = RecommendationService.findDestination(weatherObjects);

        RecommendationService.findHotel(chosenIsland, hotelObjects);

    }
}