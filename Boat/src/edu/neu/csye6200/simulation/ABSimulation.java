package edu.neu.csye6200.simulation;

import edu.neu.csye6200.ui.ABSimulationUI;
import edu.neu.csye6200.simulation.rule.ABRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class ABSimulation extends Observable implements Runnable {
    private Thread thread = null; // the thread that runs my simulation

    private Boat boat;
    private ABRule rule;
    private OceanGrid oceanGrid;
    private int simulationCycleLimit;

    public void start() {
        if (thread != null) return; // A thread is already running
        thread = new Thread(this); // Create a worker thread
        thread.start();
    }

    public void stop() {
        // Set remaining cycles to 0
        simulationCycleLimit = 0;
    }

    public void update() {
        setChanged();
        notifyObservers(this); // Send a copy of the simulation
    }

    public boolean shouldDrawOceanGrid() {
        return boat != null && oceanGrid != null;
    }

    @Override
    public void run() {
        update();

        boolean isOceanGridCleaned = false;

        // Stop simulation while ocean is cleaned or simulation cycle limit reached
        while(!isOceanGridCleaned && simulationCycleLimit > 0) {
            System.out.println("Remaining cycles " + simulationCycleLimit);
            sleep(500);

            // Get best cleaning route for this cycle
            List<Position> bestCleaningRoute = rule.getBestCleaningRoute(boat, oceanGrid);

            // Clean contaminate area based on the movement of boat
            oceanGrid.cleanContaminateArea(bestCleaningRoute);

            // Update contaminate area based on existing contaminate cells and the flow rate
            oceanGrid.updateContaminateStatus();

            // Update boat position as the last cell of the cleaning route
            boat.setPosition(bestCleaningRoute.get(bestCleaningRoute.size() - 1));

            // Update terminate criteria
            isOceanGridCleaned = oceanGrid.isOceanGridCleaned();
            simulationCycleLimit--;

            update();
        }

        thread = null;
    }

    /**
     * Make the current thread sleep a little
     * @param millis the time to sleep before the thread may re-awaken
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public void setRule(ABRule rule) {
        this.rule = rule;
    }

    public void setOceanGrid(OceanGrid oceanGrid) {
        this.oceanGrid = oceanGrid;
    }

    public OceanGrid getOceanGrid() {
        return oceanGrid;
    }

    public void setSimulationCycleLimit(int simulationCycleLimit) {
        this.simulationCycleLimit = simulationCycleLimit;
    }
}
