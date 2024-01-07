package dacd.navarro.control;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class AMQTopicWeatherSubscriber implements Subscriber, MessageListener, AutoCloseable {
    private static String url;
    private static String topicName;

    private static Connection connection;
    private static Session session;
    private static List<String> eventWeatherList = new ArrayList<>();
    private static String subscriberName;

    public AMQTopicWeatherSubscriber(String topicName, String subscriberName) throws JMSException {
        this.subscriberName = subscriberName;
        this.topicName = topicName;

        url = ActiveMQConnection.DEFAULT_BROKER_URL;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.setClientID(subscriberName);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void subscribeToTopic() throws JMSException {
        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createDurableSubscriber(topic, subscriberName);
        consumer.setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String response = ((TextMessage) message).getText();
                System.out.println("Received = " + response);

                eventWeatherList.add(response);

                if (response.equalsIgnoreCase("Quit")) {
                    connection.close();
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() throws JMSException {
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public List<String> getEventsList() {
        return eventWeatherList;
    }
}
