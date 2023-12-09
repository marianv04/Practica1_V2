package dacd.navarro.model;


import java.time.Instant;

public class Weather {
    private Instant ts;
    private String ss;
    private String predictionTime;
    private String name;
    private String location;
    private String date;
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private String description;
    private double precipitation;
    private Location locationObject;

    public Weather(Location locationObject, Instant ts, String ss, String predictionTime, String name, String location, String date, double temperature, double precipitation, int humidity, int clouds, double windSpeed, String description) {
        this.name = name;
        this.location = location;
        this.locationObject = locationObject;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.description = description;
        this.precipitation = precipitation;
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
    }

    public double getPrecipitation() {
        return precipitation;
    }


    public String getName() {
        return name;
    }


    public String getLocation() {
        return location;
    }


    public String getDate() {
        return date;
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


    public String getDescription() {
        return description;
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

    public Location getLocationObject() {
        return locationObject;
    }
}

