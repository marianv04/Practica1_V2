package dacd.navarro.control;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class AMQTopicWeatherSubscriber implements Subscriber, MessageListener {
    private static String url;
    private static String topicName;

    private static Connection connection;
    private static Session session;
    private static String subscriberName;
    private static List<String> eventWeatherList = new ArrayList<>();


    public AMQTopicWeatherSubscriber(String subscriberName, String topicName) throws JMSException {
        this.subscriberName = subscriberName;
        this.topicName = topicName;

        url = ActiveMQConnection.DEFAULT_BROKER_URL;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void subscribeToTopic() throws JMSException {
        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createConsumer(topic);
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
