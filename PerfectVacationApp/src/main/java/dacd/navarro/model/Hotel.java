package dacd.navarro.model;

public class Hotel {
    private String island;
    private String name;
    private double price;
    private int stars;
    private double score;

    private String id;
    private double latitude;
    private double longitude;

    public Hotel(String island, String id, String name, double price, int stars, double score, double latitude, double longitude) {
        this.island = island;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stars = stars;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIsland() {
        return island;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }
}
