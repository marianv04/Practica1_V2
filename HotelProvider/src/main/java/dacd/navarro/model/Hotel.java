package dacd.navarro.model;

import java.time.Instant;

public class Hotel {
    private Instant ts;
    private String ss;
    private String island;
    private String id;
    private String name;
    private double price;
    private int stars;
    private double score;

    private String themeTemp;
    private double latitude;
    private double longitude;

    public Hotel(Instant ts, String ss, String island, String id, String name, double price, int stars, double score, String themeTemp, double latitude, double longitude) {
        this.ts = ts;
        this.ss = ss;
        this.island = island;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stars = stars;
        this.score = score;
        this.themeTemp = themeTemp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }

    public String getIsland() {
        return island;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStars() {
        return stars;
    }

    public double getScore() {
        return score;
    }

    public String getThemeTemp() {
        return themeTemp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
