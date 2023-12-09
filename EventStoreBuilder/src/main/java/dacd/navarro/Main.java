package dacd.navarro;

import javax.jms.JMSException;


public class Main {
    public static void main(String[] args) throws JMSException {
        String subscriberName = "prediction-provider";
        String topicName = "prediction.Weather";
        Subscriber topicSubscriber = new AMQTopicSubscriber(subscriberName, topicName);
        topicSubscriber.subscribeToTopic();
    }
}