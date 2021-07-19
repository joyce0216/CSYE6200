package edu.neu.csye6200.simulation.rule;

import edu.neu.csye6200.simulation.Boat;
import edu.neu.csye6200.simulation.OceanGrid;
import edu.neu.csye6200.simulation.Position;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomABRule extends AbstractABRule {
    /**
     * Given the existing status of OceanGrid and location of boat, decides the best cleaning route
     * which maximize the sum of contaminated values cleaned by rolling a dice
     *
     * @param boat initial boat position
     * @param oceanGrid ocean status
     * @return A list of positions in order represents the best cleaning route
     */
    @Override
    public List<Position> getBestCleaningRoute(Boat boat, OceanGrid oceanGrid) {
        List<Position> bestRoute = new ArrayList<>();
        Set<Position> visitedPositions = new HashSet<>();

        int remainingMoves = boat.getSpeed() + 1;
        Position currentPosition = boat.getPosition();
        List<List<Integer>> contaminateStatus = oceanGrid.getContaminateStatus();

        while(remainingMoves > 0) {
            visitedPositions.add(currentPosition);
            bestRoute.add(currentPosition);

            // Find the next cell to move to from adjacent cells, based on the largest contaminate value
            List<Position> nextCandidates = Arrays.asList(currentPosition.getUpPosition(),
                    currentPosition.getDownPosition(), currentPosition.getLeftPosition(), currentPosition.getRightPosition());
            nextCandidates = nextCandidates.stream()
                    .filter(c -> !isPositionOutOfBounds(contaminateStatus, c) && !visitedPositions.contains(c))
                    .collect(Collectors.toList());

            currentPosition = nextCandidates.get(ThreadLocalRandom.current().nextInt(0, nextCandidates.size()));

            remainingMoves--;
        }

        return bestRoute;
    }
}
