package edu.neu.csye6200.simulation.rule;

import edu.neu.csye6200.simulation.Position;

import java.util.List;

public abstract class AbstractABRule implements ABRule {
    /**
     * Is a position within the ocean grid
     * @param contaminateStatus 2D matrix of ocean grid
     * @param currentPosition position
     * @return Is a position within the ocean grid
     */
    boolean isPositionOutOfBounds(List<List<Integer>> contaminateStatus, Position currentPosition) {
        return currentPosition.getX() < 0 || currentPosition.getY() < 0
                || currentPosition.getX() >= contaminateStatus.get(0).size()
                || currentPosition.getY() >= contaminateStatus.size();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
