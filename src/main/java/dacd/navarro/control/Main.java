package dacd.navarro.control;

import java.text.ParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws ParseException {

        Timer timer = new Timer();

        long delay = 0;
        long period = 6 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {

                    WeatherDataController.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }
}