package dacd.navarro.model;

public class Weather {
    private String name;
    private String location;
    private String date;
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private String description;
    private double precipitation;

    public Weather(String name, String location, String date, double temperature, double precipitation, int humidity, int clouds, double windSpeed, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.description = description;
        this.precipitation = precipitation;
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

}
