package dacd.navarro.control;

public interface WeatherDataStoreManagerInterface {
    void storeData(String name, String cityName, String date, double temperature, double precipitation, int humidity, int clouds, double wind_speed, String description);
}