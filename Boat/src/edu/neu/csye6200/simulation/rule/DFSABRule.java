package edu.neu.csye6200.simulation.rule;

import edu.neu.csye6200.simulation.Boat;
import edu.neu.csye6200.simulation.OceanGrid;
import edu.neu.csye6200.simulation.Position;

import java.util.*;

public class DFSABRule extends AbstractABRule {
    /**
     * Given the existing status of OceanGrid and location of boat, decides the best cleaning route
     * which maximize the sum of contaminated values cleaned using DFS (depth first search)
     * @param boat initial boat position
     * @param oceanGrid ocean status
     * @return A list of positions in order represents the best cleaning route
     */
    @Override
    public List<Position> getBestCleaningRoute(Boat boat, OceanGrid oceanGrid) {
        List<Position> bestRoute = new ArrayList<>();

        // Reset visited cells
        Set<Position> visitedCell = new HashSet<>();

        // Run DFS to find the best route which maximize the total contaminated values cleaned
        dfs(oceanGrid.getContaminateStatus(), visitedCell, bestRoute, boat.getSpeed() + 1, boat.getPosition());

        return bestRoute;
    }

    /**
     * Use DFS (depth first search)
     * @param contaminateStatus the contaminateStatus of the oceanGrid
     * @param visitedCells not visited a cell twice
     * @param bestRoute best routes
     * @param remainingMoves tracks the remaining moves of the boat
     * @param currentPosition current position of the boat
     * @return Sum of contaminates cleaned
     */
    private int dfs(List<List<Integer>> contaminateStatus, Set<Position> visitedCells, List<Position> bestRoute,
                    int remainingMoves, Position currentPosition) {
        // Terminate if
        // 1. cell has been visited
        // 2. beyond the speed of the boat
        // 3. position is out of bounds
        if(visitedCells.contains(currentPosition)
                || remainingMoves == 0
                || isPositionOutOfBounds(contaminateStatus, currentPosition)) {
            return 0; // nothing cleaned
        }

        // Mark current position as visited
        visitedCells.add(currentPosition);
        // Reduce one move from the remaining moves
        remainingMoves--;

        // Search for the best sub route which maximize the total cleaning value
        List<Position> leftSubRoute = new ArrayList<>();
        int leftSum = dfs(contaminateStatus, new HashSet<>(visitedCells), leftSubRoute, remainingMoves, currentPosition.getLeftPosition());

        List<Position> rightSubRoute = new ArrayList<>();
        int rightSum = dfs(contaminateStatus, new HashSet<>(visitedCells), rightSubRoute, remainingMoves, currentPosition.getRightPosition());

        List<Position> upSubRoute = new ArrayList<>();
        int upSum = dfs(contaminateStatus, new HashSet<>(visitedCells), upSubRoute, remainingMoves, currentPosition.getUpPosition());

        List<Position> downSubRoute = new ArrayList<>();
        int downSum = dfs(contaminateStatus, new HashSet<>(visitedCells), downSubRoute, remainingMoves, currentPosition.getDownPosition());

        // Decide which sub-route to select based on the sum of cleaned values
        Map<Integer, List<Position>> sumToRouteMap = new HashMap<>();
        sumToRouteMap.put(leftSum, leftSubRoute);
        sumToRouteMap.put(rightSum, rightSubRoute);
        sumToRouteMap.put(upSum, upSubRoute);
        sumToRouteMap.put(downSum, downSubRoute);

        int maxSum = Collections.max(Arrays.asList(leftSum, rightSum, upSum, downSum));
        bestRoute.add(0, currentPosition);
        bestRoute.addAll(sumToRouteMap.get(maxSum));

        return maxSum + contaminateStatus.get(currentPosition.getY()).get(currentPosition.getX());
    }
}
