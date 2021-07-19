package edu.neu.csye6200.simulation;

import java.util.ArrayList;
import java.util.List;

public class OceanGrid {
    // Y axis of the grid
    private int height;

    // X axis of the grid
    private int width;

    // Current contaminated status of this OceanGrid, size is length by width
    // (0, 0) --> (width, height) is from bottom-left to top-right of the grid
    // The contaminate value at a cell is [0, 100]
    private List<List<Integer>> contaminateStatus;

    // spread value to adjacent cells (vertical and horizontal) based on current value
    // range from 0.0 to 1.0
    private double flowRate;

    /**
     * Initialize the OceanGrid with size and flowRate
     * @param contaminateStatus 2D matrix with contaminate values at each cell
     * @param flowRate spread rate to adjacent cells (vertical and horizontal) based on current value
     */
    public OceanGrid(List<List<Integer>> contaminateStatus, double flowRate) {
        this.height = contaminateStatus.size();
        this.width = contaminateStatus.get(0).size();
        this.contaminateStatus = contaminateStatus;
        this.flowRate = flowRate;
    }

    /**
     * Set positions from boatCleaningRoute to zero (not contaminated) in the contaminateStatus
     * @param boatCleaningRoute Boat cleaning route from current simulation cycle
     */
    public void cleanContaminateArea(List<Position> boatCleaningRoute) {
        for (Position position: boatCleaningRoute){
            contaminateStatus.get(position.getY()).set(position.getX(), 0);
        }
    }

    /**
     * Update ContaminateStatus based on current status and flow rate, values are grounded to integer
     */
    public void updateContaminateStatus() {
        List<List<Integer>> updatedContaminateStatus = new ArrayList<>();
        for(int y = 0; y < height; y++) {
            List<Integer> row = new ArrayList<>();
            for(int x = 0; x < width; x++) {
                row.add(0);
            }
            updatedContaminateStatus.add(row);
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                double currentValue = contaminateStatus.get(y).get(x);
                // up cell
                if (y+1 < height) {
                    currentValue += contaminateStatus.get(y+1).get(x) * flowRate;
                }
                // down cell
                if (y-1 >= 0) {
                    currentValue += contaminateStatus.get(y-1).get(x) * flowRate;
                }
                // left cell
                if (x-1 >= 0) {
                    currentValue += contaminateStatus.get(y).get(x-1) * flowRate;
                }
                // right cell
                if (x+1 < width) {
                    currentValue += contaminateStatus.get(y).get(x+1) * flowRate;
                }

                // Update contaminate value of current cell, max at 100
                // Ground to integer
                updatedContaminateStatus.get(y).set(x, Math.min((int) currentValue, 100));
            }
        }

        contaminateStatus = updatedContaminateStatus;
    }

    /**
     * Check if all cells in the grid have contaminate value of zero
     * @return true: OceanGrid is Cleaned; false: OceanGrid is not cleaned
     */
    public boolean isOceanGridCleaned() {
        for(List<Integer> row:contaminateStatus) {
            for(int status:row) {
                if(status != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Get current contaminateStatus
     * @return 2D map of contaminate values of each cells in the grid
     */
    public List<List<Integer>> getContaminateStatus() {
        return this.contaminateStatus;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
