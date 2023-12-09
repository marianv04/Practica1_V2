package dacd.navarro;

import javax.jms.JMSException;

public interface Listener {
    void consume(String message) throws JMSException;
}
