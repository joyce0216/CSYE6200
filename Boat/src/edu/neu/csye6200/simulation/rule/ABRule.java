package edu.neu.csye6200.simulation.rule;

import edu.neu.csye6200.simulation.Boat;
import edu.neu.csye6200.simulation.OceanGrid;
import edu.neu.csye6200.simulation.Position;

import java.util.List;

public interface ABRule {
    /**
     * Given the existing status of OceanGrid and location of boat, decides the best cleaning route for this loop
     * @param boat
     * @param oceanGrid
     * @return A list of positions in order represents the best cleaning route
     */
    List<Position> getBestCleaningRoute(Boat boat, OceanGrid oceanGrid);
}
