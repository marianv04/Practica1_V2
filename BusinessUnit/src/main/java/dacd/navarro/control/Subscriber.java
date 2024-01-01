package dacd.navarro.control;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;

public interface Subscriber {
    void subscribeToTopic() throws JMSException;
    void onMessage(Message message);

    void close() throws JMSException;
    List<String> getEventsList();
}
