package dacd.navarro.model;

public class Weather {
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private double precipitation;
    private String island;
    private String predictionTime;

    public Weather(String island, String predictionTime, double temperature, double precipitation, int humidity, int clouds, double windSpeed) {
        this.island = island;
        this.predictionTime = predictionTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
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

    public String getPredictionTime() {
        return predictionTime;
    }

    public String getIsland() {
        return island;
    }
}
