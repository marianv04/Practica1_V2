package dacd.navarro.model;
import java.sql.Timestamp;
import java.time.Instant;
public class Weather {
    private Instant ts;
    private String ss;
    private Timestamp predictionTs;
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private double precipitation;
    private String island;
    private Location location;

    public Weather(Location location, Instant ts, String ss, String island, Timestamp predictionTs, double temperature, double precipitation, int humidity, int clouds, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.ts = ts;
        this.ss = ss;
        this.island = island;
        this.predictionTs = predictionTs;
    }

    public double getPrecipitation() {
        return precipitation;
    }


    public double getTemperature() {
        return temperature;
    }


    public int getHumidity() {
        return humidity;
    }


    public int getClouds() {
        return clouds;
    }


    public double getWindSpeed() {
        return windSpeed;
    }


    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }

    public Timestamp getPredictionTs() {
        return predictionTs;
    }

    public String getIsland() {
        return island;
    }

    public Location getLocation() {
        return location;
    }
}

