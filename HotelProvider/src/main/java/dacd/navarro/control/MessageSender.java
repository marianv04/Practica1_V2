package dacd.navarro.control;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String topicName = "search.Hotel";

    public static void messageToBroker(String serializedObject) throws JMSException {
        Connection connection = getConnection();

        sendMessage(connection, serializedObject);

        closeConnection(connection);
    }

    private static Connection getConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private static void sendMessage(Connection connection, String serializedObject) throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);

        TextMessage message = session.createTextMessage(serializedObject);

        producer.send(message);

        System.out.println("Sent message: '" + message.getText() + "'");
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


}
