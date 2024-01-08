package dacd.navarro.control;

import dacd.navarro.model.Hotel;
import dacd.navarro.model.Weather;
import dacd.navarro.view.RecommendationService;

import javax.jms.JMSException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JMSException {
        List<Weather> weatherObjects;
        List<Hotel> hotelObjects;

        weatherObjects = WeatherEventsProcessor.saveWeatherInDatamart(args[0]);
        hotelObjects = HotelEventsProcessor.saveHotelInDatamart(args[0]);

        String chosenIsland = RecommendationService.findDestination(weatherObjects);

        RecommendationService.findHotel(chosenIsland, hotelObjects);
    }
}