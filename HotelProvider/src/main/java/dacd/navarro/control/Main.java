package dacd.navarro.control;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();

        long delay = 0;
        long period = 6 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String apiKey = args[0];
                    HotelController.execute(apiKey);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }
}