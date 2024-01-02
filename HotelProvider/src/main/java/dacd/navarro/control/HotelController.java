package dacd.navarro.control;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.jms.JMSException;
import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class HotelController {
    public static void execute(String apiKey) throws JMSException {
        List<String> islands;
        Provider dataProvider = new HotelProvider();
        islands = readFile();
        for(int i = 0; i < islands.size(); i++){
            int regionId = HotelApiConnector.getRegionId(islands.get(i), apiKey);
            String response = HotelApiConnector.getHotels(regionId, apiKey, "2024-09-09", "2024-10-10", 1);
            List<String> hotelSerializedList = dataProvider.getHotelInfo(response, islands.get(i));

            for(String hotelObjectSerialized : hotelSerializedList){

                MessageSender.messageToBroker(hotelObjectSerialized);
                System.out.println("Message sent:" + hotelObjectSerialized);
            }
            System.out.println("Hotel list from " + islands.get(i) + " added.");

        }
    }


    public static List<String> readFile(){
        List<String> islands = new ArrayList<>();
        try (InputStream inputStream = HotelController.class.getClassLoader().getResourceAsStream("Locations.csv")) {
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader reader = new CSVReader(inputStreamReader);

                String[] line;
                while ((line = reader.readNext()) != null) {
                    String name = line[0];
                    islands.add(name);
                }

            } else {
                throw new IOException("Locations.csv could not be loaded.");
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return islands;
    }
}
