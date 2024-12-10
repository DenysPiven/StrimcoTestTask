package org.example;

import org.example.data.Reader;
import org.example.logic.TeamSelector;
import org.example.model.Player;
import org.example.model.Position;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        List<Player> players = reader.readPlayersFromCSV("Data.csv");

        TeamSelector selector = new TeamSelector();
        Map<Position, List<Player>> team = selector.selectTeam(players);

        System.out.println("Selected team:");
        AtomicReference<Double> totalRating = new AtomicReference<>(0.0);

        // Сортуємо записи команди за порядком, визначеним у Position
        team.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((p1, p2) -> Integer.compare(p1.getOrder(), p2.getOrder())))
                .forEach(entry -> {
                    Position pos = entry.getKey();
                    for (Player p : entry.getValue()) {
                        Double rating = pos == Position.ST ? p.getSTRating() : p.getRatingForPosition(pos);
                        totalRating.updateAndGet(v -> v + rating);

                        // Форматований вивід гравця
                        System.out.println("Position: " + pos +
                                " | Player: " + p.getName() +
                                " | Nation: " + p.getPrimaryNation() +
                                " | Club: " + p.getClub() +
                                " | Positions: " + p.getPositions() +
                                " | Rating: " + rating);
                    }
                });

        System.out.println("Total Team Rating: " + totalRating);
    }
}
