package org.example.logic;

import org.example.model.Player;
import org.example.model.Position;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TeamSelector {
    private static final Logger LOGGER = Logger.getLogger(TeamSelector.class.getName());
    private static final Map<Position, Integer> REQUIRED_POSITIONS = new LinkedHashMap<>();

    static {
        REQUIRED_POSITIONS.put(Position.GK, 1);
        REQUIRED_POSITIONS.put(Position.DL, 1);
        REQUIRED_POSITIONS.put(Position.DC, 2); // 2 центральні захисники
        REQUIRED_POSITIONS.put(Position.DR, 1);
        REQUIRED_POSITIONS.put(Position.MC, 2); // 2 центральні півзахисники
        REQUIRED_POSITIONS.put(Position.AML, 1);
        REQUIRED_POSITIONS.put(Position.AMC, 1);
        REQUIRED_POSITIONS.put(Position.AMR, 1);
        REQUIRED_POSITIONS.put(Position.ST, 1);
    }

    private volatile double bestTotalRating = 0.0;
    private final Map<Position, List<Player>> bestSolution = new HashMap<>();

    public Map<Position, List<Player>> selectTeam(List<Player> players) {
        Map<Position, List<Player>> candidatesMap = prepareCandidates(players, 100);

        LOGGER.info("Starting team selection...");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Void> future = executor.submit(() -> {
            backtrack(new ArrayList<>(REQUIRED_POSITIONS.keySet()), 0, candidatesMap, new HashSet<>(), new HashSet<>(), new HashMap<>(), 0.0);
            return null;
        });

        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            LOGGER.warning("Time limit exceeded. Returning the best result found so far.");
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.severe("Error during team selection: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }

        LOGGER.info("Team selection completed. Best rating: " + bestTotalRating);
        return bestSolution;
    }

    private Map<Position, List<Player>> prepareCandidates(List<Player> players, int maxCandidates) {
        Map<Position, List<Player>> candidatesMap = new HashMap<>();

        for (Position pos : REQUIRED_POSITIONS.keySet()) {
            List<Player> positionCandidates = players.stream()
                    .filter(p -> p.getPositions().contains(pos))
                    .sorted((p1, p2) -> Double.compare(p2.getRatingForPosition(pos), p1.getRatingForPosition(pos)))
                    .limit(maxCandidates)
                    .collect(Collectors.toList());

            LOGGER.info("Prepared " + positionCandidates.size() + " candidates for position: " + pos);
            candidatesMap.put(pos, positionCandidates);
        }

        return candidatesMap;
    }

    private void backtrack(List<Position> positionsOrder, int index, Map<Position, List<Player>> candidatesMap,
                           Set<String> usedNations, Set<String> usedClubs, Map<Position, List<Player>> currentSelection,
                           double currentRating) {
        if (index == positionsOrder.size()) {
            updateBestSolution(currentRating, currentSelection);
            return;
        }

        Position currentPosition = positionsOrder.get(index);
        List<Player> candidates = candidatesMap.get(currentPosition);
        int requiredCount = REQUIRED_POSITIONS.get(currentPosition);

        for (Player candidate : candidates) {
            String nation = candidate.getPrimaryNation();
            String club = candidate.getClub();

            if (!usedNations.contains(nation) && !usedClubs.contains(club)) {
                usedNations.add(nation);
                usedClubs.add(club);
                currentSelection.computeIfAbsent(currentPosition, k -> new ArrayList<>()).add(candidate);

                double candidateRating = currentPosition == Position.ST
                        ? candidate.getSTRating()
                        : candidate.getRatingForPosition(currentPosition);

                if (currentSelection.get(currentPosition).size() < requiredCount) {
                    // Рекурсія, якщо ще не досягнуто потрібної кількості гравців
                    backtrack(positionsOrder, index, candidatesMap, usedNations, usedClubs, currentSelection, currentRating + candidateRating);
                } else {
                    // Перейти до наступної позиції
                    backtrack(positionsOrder, index + 1, candidatesMap, usedNations, usedClubs, currentSelection, currentRating + candidateRating);
                }

                // Видаляємо гравця після рекурсії
                usedNations.remove(nation);
                usedClubs.remove(club);
                currentSelection.get(currentPosition).remove(candidate);
            }
        }
    }

    private void updateBestSolution(double currentRating, Map<Position, List<Player>> currentSelection) {
        synchronized (this) {
            if (currentRating > bestTotalRating) {
                bestTotalRating = currentRating;
                bestSolution.clear();
                currentSelection.forEach((key, value) -> bestSolution.put(key, new ArrayList<>(value)));
                LOGGER.info("New best rating: " + bestTotalRating);
            }
        }
    }
}
