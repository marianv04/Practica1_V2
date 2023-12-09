package dacd.navarro;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AMQTopicSubscriber implements Subscriber, MessageListener {
    private static String url;
    private static String topicName;

    private static Connection connection;
    private static Session session;
    private static String subscriberName;

    public AMQTopicSubscriber(String subscriberName, String topicName) throws JMSException {
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

        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            connection.close();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String response = ((TextMessage) message).getText();
                System.out.println("Received = " + response);

                if (response.equalsIgnoreCase("Quit")) {
                    synchronized (this) {
                        notify();
                    }
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
