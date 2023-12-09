package dacd.navarro;

import com.google.gson.*;

import javax.jms.JMSException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileEventStore implements Listener{
    public static List<String> directoryCreator(String json){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        String tsString = jsonObject.get("ts").getAsString();
        String ss = jsonObject.get("ss").getAsString();

        Instant ts = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(tsString));

        LocalDateTime tsLocalDateTime = LocalDateTime.ofInstant(ts, ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedTs = tsLocalDateTime.format(formatter);
        System.out.println("eventstore" + "/" + "prediction.Weather" + "/" + ss + "/");
        List<String> directory = new ArrayList<>();
        directory.add("eventstore" + "/" + "prediction.Weather" + "/" + ss + "/");
        directory.add(formattedTs);

        return directory;
    }

    public static void storeEventInFile(String json, List<String> directoryPath) {
        try {
            Path directory = Paths.get(directoryPath.get(0));
            if (!directory.toFile().exists()) {
                directory.toFile().mkdirs();
            }

            String fileName = directoryPath.get(1) + ".events";
            Path filePath = Paths.get(directoryPath.get(0)).resolve(fileName);

            try (FileWriter fileWriter = new FileWriter(filePath.toString(), true)) {
                System.out.println("File created.");
                fileWriter.write(json);
                fileWriter.write(System.lineSeparator());
                System.out.println("The serialized event has been written in the file.");
            }

            System.out.println("Event successfully stored in the Event Store.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void consume(String message) throws JMSException {
        List<String> directoryPath = directoryCreator(message);
        storeEventInFile(message, directoryPath);
    }

}