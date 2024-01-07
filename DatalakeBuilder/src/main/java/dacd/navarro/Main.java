package dacd.navarro;


import javax.jms.JMSException;


public class Main {
    public static void main(String[] args) throws JMSException {
        String subscriberNameWeather = "prediction-provider";
        String topicNameWeather = "prediction.Weather";
        String subscriberNameHotel = "hotel-provider";
        String topicNameHotel = "search.Hotel";
        Subscriber topicSubscriberWeather = new AMQTopicWeatherSubscriber(subscriberNameWeather, topicNameWeather, args[0]);
        topicSubscriberWeather.subscribeToTopic();
        Subscriber topicSubscriberHotel = new AMQTopicHotelSubscriber(subscriberNameHotel, topicNameHotel, args[0]);
        topicSubscriberHotel.subscribeToTopic();
    }
}