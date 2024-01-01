package dacd.navarro.model;


import java.time.Instant;

public class Weather {
    private Instant ts;
    private String ss;
    private String predictionTime;
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private double precipitation;
    private Location location;

    public Weather(Location location, Instant ts, String ss, String predictionTime, double temperature, double precipitation, int humidity, int clouds, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
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

    public String getPredictionTime() {
        return predictionTime;
    }

    public Location getLocation() {
        return location;
    }
}

