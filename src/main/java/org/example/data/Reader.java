package org.example.data;

import org.example.model.Player;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    public List<Player> readPlayersFromCSV(String filePath) {
        List<Player> players = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! " + filePath);
            }

            try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
                String[] values;
                csvReader.readNext();

                while ((values = csvReader.readNext()) != null) {
                    String name = values[0].trim();
                    String nation = values[1].trim();
                    String position = values[2].trim();
                    String club = values[3].trim();
                    Integer age = Integer.valueOf(values[4].trim());

                    Double GK = parsePercent(values[5]);
                    Double DL = parsePercent(values[6]);
                    Double DC = parsePercent(values[7]);
                    Double DR = parsePercent(values[8]);
                    Double DM = parsePercent(values[9]);
                    Double ML = parsePercent(values[10]);
                    Double MC = parsePercent(values[11]);
                    Double MR = parsePercent(values[12]);
                    Double AML = parsePercent(values[13]);
                    Double AMC = parsePercent(values[14]);
                    Double AMR = parsePercent(values[15]);
                    Double FS = parsePercent(values[16]);
                    Double TS = parsePercent(values[17]);

                    Player p = new Player(name, nation, position, club, age,
                            GK, DL, DC, DR, DM, ML, MC, MR, AML, AMC, AMR, FS, TS);
                    players.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }

    private Double parsePercent(String val) {
        String clean = val.replace("%", "").trim();
        return Double.valueOf(clean);
    }
}
