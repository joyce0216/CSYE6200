package edu.neu.csye6200.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.neu.csye6200.simulation.ABSimulation;
import edu.neu.csye6200.simulation.Position;

/**
 * @author Joyce Gu
 *
 */
public class MyCanvas extends JPanel implements Observer {
    private ABSimulation abSimulation;

    public MyCanvas(ABSimulation abSimulation)  {
        this.abSimulation = abSimulation;
    }

    // Swing calls when a redraw is needed
    @Override
    public void paint(Graphics g) {
        drawCanvas(g);
    }

    // Draw the contents of the panel
    private void drawCanvas(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, size.width, size.height);

        if(abSimulation.shouldDrawOceanGrid()) {
            drawOceanGrid(g2d);
        }
    }

    private void drawOceanGrid(Graphics2D g2d) {
        int lineSize = 50; // How big each cell should be

        Dimension size = getSize();
        int maxRows = abSimulation.getOceanGrid().getHeight();
        int maxCols = abSimulation.getOceanGrid().getWidth();

        List<List<Integer>> contaminateArea = abSimulation.getOceanGrid().getContaminateStatus();
        for (int j = 0; j < maxRows; j++) {
            for (int i = 0; i < maxCols; i++) {
                g2d.setColor(getColorByPercentage(Color.WHITE, contaminateArea.get(j).get(i)));
                int start = i * lineSize;
                // Draw in reverse order
                g2d.fillRect(start, (maxRows - j - 1) * lineSize, lineSize-1, lineSize-1);
            }
        }

        // set boat position to yellow
        Position boatPosition = abSimulation.getBoat().getPosition();
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(boatPosition.getX() * lineSize, (maxRows - boatPosition.getY() - 1) * lineSize, lineSize-1, lineSize-1);
    }

    /**
     * Get color based on the contaminate value from 0 to 100
     * @param baseColor
     * @param percentage
     * @return white if percentage is 0, black if percentage is 100
     */
    private Color getColorByPercentage(Color baseColor, int percentage) {
        float[] rgba = baseColor.getRGBComponents(null);
        float red = rgba[0]/100*(100-percentage);
        float green = rgba[1]/100*(100-percentage);
        float blue = rgba[2]/100*(100-percentage);

        return new Color(red, green, blue);
    }

    @Override
    public void update(Observable o, Object arg) {
        abSimulation = (ABSimulation) o;
        repaint(); // Tell the GUI thread that it should schedule a paint() call
    }
}

