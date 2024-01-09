package dacd.navarro.view;

import dacd.navarro.model.Hotel;
import dacd.navarro.model.Weather;

import java.util.*;

public class RecommendationService {
    public static String findDestination(List<Weather> weatherObjects) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now that all data has been downloaded we can start to search your most suitable destinations.\n");
        System.out.println("First of all, lets see which island matches your climatic preferences the best.\n");
        System.out.println("¿Do you prefer a colder (<16), normal(>=16 and <=20) or hotter(>20) temperature? (Please answer with one word: colder, normal or hotter)\n");
        String userTemp = scanner.nextLine();
        List<Weather> userTempList = chooseTemperature(userTemp, weatherObjects);
        String chosenIsland = null;
        if (chooseIsland(userTempList) != null){
            chosenIsland = chooseIsland(userTempList);
        }

        System.out.println("¿Do you prefer a cloudless sky(<=10) or lots of clouds(>10)? (Please answer one of this two options: cloudless or lots of clouds) \n");
        String userClouds = scanner.nextLine();
        List<Weather> userCloudsList;
        if(userTempList.size() == 0){
            userCloudsList = chooseClouds(userClouds, weatherObjects);
        } else {
            userCloudsList = chooseClouds(userClouds, userTempList);
        }

        if (chooseIsland(userCloudsList) != null){
            chosenIsland = chooseIsland(userCloudsList);
        }

        System.out.println("¿Do you prefer a rainy location(>=0.5) or a dry one (<0.5)? (Please answer one of this two options: rainy or dry) \n");
        String userPrecipitation = scanner.nextLine();

        List<Weather> userPrecipitationList;
        if(userCloudsList.size() == 0){
            userPrecipitationList = choosePrecipitation(userPrecipitation, userTempList);
        } else {
            userPrecipitationList = choosePrecipitation(userPrecipitation, userCloudsList);
        }

        if (chooseIsland(userPrecipitationList) != null){
            chosenIsland = chooseIsland(userPrecipitationList);
        }

        if(chosenIsland != null){
            System.out.println("Now, on the basis of your answers we can determine that the most suitable destination for you is: " + chosenIsland + "\n");
            System.out.println("¿Do you want to continue the process with this island or do you want to choose a new one? (Please answer with continue or choose)\n");
            String confirmation = scanner.nextLine();
            if ("continue".equals(confirmation)){
                System.out.println("You will continue with " + chosenIsland + ".");
            } else {
                System.out.println("You are not happy with the island chosen, so, ¿Which island do you want to choose? (Please answer with one of this options: Gran_Canaria, Tenerife, La_Gomera, La_Graciosa, Lanzarote, Fuerteventura, La_Palma or El_Hierro.\n");
                chosenIsland = scanner.nextLine();
                System.out.println("You chose " + chosenIsland + ".");
            }
        } else {
            System.out.println("There is not an island that fulfills your preferences. ¿Which island do you want to choose? (Please answer with one of this options: Gran_Canaria, Tenerife, La_Gomera, La_Graciosa, Lanzarote, Fuerteventura, La_Palma or El_Hierro.\n");
            chosenIsland = scanner.nextLine();
            System.out.println("You chose " + chosenIsland + ".");
        }
        return chosenIsland;
    }

    public static List<Weather> chooseTemperature(String userTemp, List<Weather> weatherObjects) {
        List<Weather> userTempList = new ArrayList<>();
        if ("colder".equals(userTemp)) {
            for (Weather weather : weatherObjects) {
                double temp = weather.getTemperature();
                if (temp < 16) {
                    userTempList.add(weather);
                }
            }
            System.out.println("You chose colder temperatures.");
        }
        if ("normal".equals(userTemp)) {
            for (Weather weather : weatherObjects) {
                double temp = weather.getTemperature();
                if (temp >= 16 && temp <= 20) {
                    userTempList.add(weather);
                }
            }
            System.out.println("You chose normal temperatures.");
        }
        if ("hotter".equals(userTemp)) {
            for (Weather weather : weatherObjects) {
                double temp = weather.getTemperature();
                if (temp > 20) {
                    userTempList.add(weather);
                }
            }
            System.out.println("You chose hotter temperatures.");
        }
        return userTempList;
    }

    public static List<Weather> chooseClouds(String userClouds, List<Weather> userTempList) {
        List<Weather> userCloudsList = new ArrayList<>();
        if ("cloudless".equals(userClouds)) {
            for (Weather weather : userTempList) {
                int clouds = weather.getClouds();
                if (clouds <= 10) {
                    userCloudsList.add(weather);
                }
            }
            System.out.println("You chose a cloudless sky.");
        }
        if ("lots of clouds".equals(userClouds)) {
            for (Weather weather : userTempList) {
                int clouds = weather.getClouds();
                if (clouds > 10) {
                    userCloudsList.add(weather);
                }
            }
            System.out.println("You chose a sky with lots of clouds.");
        }
        return userCloudsList;
    }

    public static List<Weather> choosePrecipitation(String userPrecipitation, List<Weather> userCloudsList) {
        List<Weather> userPrecipitationList = new ArrayList<>();
        if ("rainy".equals(userPrecipitation)) {
            for (Weather weather : userCloudsList) {
                double rain = weather.getPrecipitation();
                if (rain >= 0.5) {
                    userPrecipitationList.add(weather);
                }
            }
            System.out.println("You chose a rainy weather.");
        }
        if ("dry".equals(userPrecipitation)) {
            for (Weather weather : userCloudsList) {
                double rain = weather.getClouds();
                if (rain < 0.5) {
                    userPrecipitationList.add(weather);
                }
            }
            System.out.println("You chose a dry weather.");
        }
        return userPrecipitationList;
    }

    public static String chooseIsland(List<Weather> userList) {
        Map<String, Integer> islandCountMap = new HashMap<>();

        for (Weather weather : userList) {
            String island = weather.getIsland();
            islandCountMap.put(island, islandCountMap.getOrDefault(island, 0) + 1);
        }

        String mostFrequentIsland = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : islandCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentIsland = entry.getKey();
            }
        }

        return mostFrequentIsland;
    }

    public static void findHotel(String chosenIsland, List<Hotel> hotelObjectsList){
        Scanner scanner = new Scanner(System.in);
        System.out.println(chosenIsland);

        System.out.println("Now we will search the best hotel for you.\n");
        List<Hotel> chosenIslandHotels = new ArrayList<>();
        for (Hotel hotel : hotelObjectsList){
            String island = hotel.getIsland();
            if(island.equals(chosenIsland)){
                chosenIslandHotels.add(hotel);
            }
        }
        System.out.println("We have " + chosenIslandHotels.size() + " options of hotels for you.\n");
        System.out.println("First of all, ¿how many stars do you want your hotel to have at least? (Please answer 1, 2, 3, 4 or 5)\n");
        String userStars = scanner.nextLine();

        List<Hotel> userStarList = chooseStars(chosenIslandHotels, userStars);

        System.out.println("¿Which price range are you willing to pay, low price (<600), normal price (>=600 and <=1000) or high price (>1000)? (Please answer with: low, normal or high)");
        String userPrice = scanner.nextLine();

        List<Hotel> userPriceList;
        if (userStarList.size() == 0) {
            userPriceList = choosePrice(chosenIslandHotels, userPrice);
        } else {
            userPriceList = choosePrice(userStarList, userPrice);
        }

        System.out.println("We have information on the average of the ratings that customers have given to hotels. What is the minimum score you want your hotel to have, at least 5, at least 7, at least 9 or the maximum punctuation of 10? (Please answer with: 5, 7, 9 or 10");
        String userScore = scanner.nextLine();

        List<Hotel> userScoreList;
        if (userPriceList.size() == 0) {
            userScoreList = chooseScore(userStarList, userScore);
        } else {
            userScoreList = chooseScore(userPriceList, userScore);
        }

        if(userScoreList.size() > 50) {
            System.out.println("There are some events that do not have information about the price per night, do yo want to be shown these hotel anyways? (Please answer with yes or no.)");
            String answer = scanner.nextLine();
            userScoreList = removeNoPriceInfo(answer, userScoreList);
        }

        System.out.println("We've recollected the best hotels for you based on your preferences.");
        if (userScoreList.size() == 0) {
            System.out.println("We could not find a hotel that is suitable for your preferences.");;
        } else {
            showHotelsInfo(userScoreList);
        }
    }

    public static List<Hotel> chooseStars(List<Hotel> chosenIslandHotels, String userStars){
        List<Hotel> userStarsList = new ArrayList<>();
        if ("1".equals(userStars)) {
            for (Hotel hotel : chosenIslandHotels) {
                int stars = hotel.getStars();
                if (stars >= 1) {
                    userStarsList.add(hotel);
                }
            }
            System.out.println("You chose at least 1 star.");
        }
        if ("2".equals(userStars)) {
            for (Hotel hotel : chosenIslandHotels) {
                int stars = hotel.getStars();
                if (stars >= 2) {
                    userStarsList.add(hotel);
                }
            }
            System.out.println("You chose at least 2 stars.");
        }
        if ("3".equals(userStars)) {
            for (Hotel hotel : chosenIslandHotels) {
                int stars = hotel.getStars();
                if (stars >= 3) {
                    userStarsList.add(hotel);
                }
            }
            System.out.println("You chose at least 3 stars.");
        }
        if ("4".equals(userStars)) {
            for (Hotel hotel : chosenIslandHotels) {
                int stars = hotel.getStars();
                if (stars >= 4) {
                    userStarsList.add(hotel);
                }
            }
            System.out.println("You chose at least 4 stars.");
        }
        if ("5".equals(userStars)) {
            for (Hotel hotel : chosenIslandHotels) {
                int stars = hotel.getStars();
                if (stars >= 5) {
                    userStarsList.add(hotel);
                }
            }
            System.out.println("You chose at least 5 stars.");
        }
        return userStarsList;
    }

    public static List<Hotel> choosePrice(List<Hotel> userScoreList, String userPrice){
        List<Hotel> userPriceList = new ArrayList<>();
        if ("low".equals(userPrice)) {
            for (Hotel hotel : userScoreList) {
                double price = hotel.getPrice();
                if (price < 600) {
                    userPriceList.add(hotel);
                }
            }
            System.out.println("You chose lower prices.");
        }
        if ("normal".equals(userPrice)) {
            for (Hotel hotel : userScoreList) {
                double price = hotel.getPrice();
                if (price >= 600 & price <= 1000) {
                    userPriceList.add(hotel);
                }
            }
            System.out.println("You chose normal prices.");
        }
        if ("high".equals(userPrice)) {
            for (Hotel hotel : userScoreList) {
                double price = hotel.getPrice();
                if (price > 1000) {
                    userPriceList.add(hotel);
                }
            }
            System.out.println("You chose higher prices.");
        }
        return userPriceList;
    }

    public static List<Hotel> chooseScore(List<Hotel> userPriceList, String userScore){
        List<Hotel> userScoreList = new ArrayList<>();
        if ("5".equals(userScore)) {
            for (Hotel hotel : userPriceList) {
                double score = hotel.getScore();
                if (score >= 5.0) {
                    userScoreList.add(hotel);
                }
            }
            System.out.println("You chose a score of at least 5.");
        }
        if ("7".equals(userScore)) {
            for (Hotel hotel : userPriceList) {
                double score = hotel.getScore();
                if (score >= 7.0) {
                    userScoreList.add(hotel);
                }
            }
            System.out.println("You chose a score of at least 7.");
        }
        if ("9".equals(userScore)) {
            for (Hotel hotel : userPriceList) {
                double score = hotel.getScore();
                if (score >= 9.0) {
                    userScoreList.add(hotel);
                }
            }
            System.out.println("You chose a score of at least 9.");
        }
        if ("10".equals(userScore)) {
            for (Hotel hotel : userPriceList) {
                double score = hotel.getScore();
                if (score >= 10.0) {
                    userScoreList.add(hotel);
                }
            }
            System.out.println("You chose a score of at least 10.");
        }
        return userScoreList;
    }

    public static List<Hotel> removeNoPriceInfo(String answer, List<Hotel> userScoreList){
        List<Hotel> removed = new ArrayList<>();
        if (answer.equals("no")){
            for (Hotel hotel : userScoreList){
                double price = hotel.getPrice();
                if(price != 0.0){
                    removed.add(hotel);
                }
            }
        } else {
            removed = userScoreList;
        }
        return removed;
    }

    public static void showHotelsInfo(List<Hotel> userScoreList) {
        if (userScoreList.isEmpty()) {
            System.out.println("No options available.");
        } else {
            List<Hotel> uniqueHotels = removeDuplicateHotels(userScoreList);

            System.out.println("Here you have " + uniqueHotels.size() + " options.\n");

            for (int i = 0; i < uniqueHotels.size(); i++) {
                Hotel hotel = uniqueHotels.get(i);
                System.out.println("Hotel number " + (i + 1) + " information:");
                System.out.println("Name: " + hotel.getName());
                System.out.println("Price: " + hotel.getPrice());
                System.out.println("Stars: " + hotel.getStars());
                System.out.println("Scores: " + hotel.getScore());
                System.out.println("Island: " + hotel.getIsland());
                System.out.println("Latitude: " + hotel.getLatitude());
                System.out.println("Longitude: " + hotel.getLongitude());
                System.out.println("  ");
            }
        }
    }

    public static List<Hotel> removeDuplicateHotels(List<Hotel> hotels) {
        List<Hotel> uniqueHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            if (!containsHotel(uniqueHotels, hotel)) {
                uniqueHotels.add(hotel);
            }
        }

        return uniqueHotels;
    }

    private static boolean containsHotel(List<Hotel> hotels, Hotel targetHotel) {
        for (Hotel hotel : hotels) {
            if (areHotelsEqual(hotel, targetHotel)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areHotelsEqual(Hotel hotel1, Hotel hotel2) {
        return hotel1.getId().equals(hotel2.getId());
    }

}
