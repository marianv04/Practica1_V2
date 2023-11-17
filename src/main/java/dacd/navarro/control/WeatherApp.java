package dacd.navarro.control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Weather Data Query App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());

            JTextField islandNameField = new JTextField(15);
            JTextField dateField = new JTextField(10);
            JButton queryButton = new JButton("Query Weather Data");

            queryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String islandName = islandNameField.getText();
                    String selectedDate = dateField.getText() + " 12:00:00";

                    WeatherDataTableQuery.fetchTableData(islandName, selectedDate);
                }
            });

            frame.add(new JLabel("Enter Island Name (Gran_Canaria, Tenerife, La_Graciosa, Lanzarote, Fuerteventura, El_Hierro, La_Palma and La_Gomera): "));
            frame.add(islandNameField);
            frame.add(new JLabel("Enter Date (YYYY-MM-DD): "));
            frame.add(dateField);
            frame.add(queryButton);

            frame.setSize(300, 150);
            frame.setVisible(true);
        });
    }
}

