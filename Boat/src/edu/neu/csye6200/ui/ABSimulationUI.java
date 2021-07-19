package edu.neu.csye6200.ui;

import edu.neu.csye6200.simulation.ABSimulation;
import edu.neu.csye6200.simulation.Boat;
import edu.neu.csye6200.simulation.OceanGrid;
import edu.neu.csye6200.simulation.Position;
import edu.neu.csye6200.simulation.rule.ABRule;
import edu.neu.csye6200.simulation.rule.BFSABRule;
import edu.neu.csye6200.simulation.rule.DFSABRule;
import edu.neu.csye6200.simulation.rule.RandomABRule;
import net.java.dev.designgridlayout.DesignGridLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
     *
     *
     */
public class ABSimulationUI extends BaseUI {
    private static final ABSimulation SIMULATION = new ABSimulation();
    private static final MyCanvas CANVAS = new MyCanvas(SIMULATION);

    private JButton startButton;
    private JButton stopButton;
    private JTextField boatPositionTextField;
    private JTextField boatSpeedTextField;
    private JTextField oceanContaminateStatusTextField;
    private JTextField contaminateFlowRateTextField;
    private JTextField simulationCycleLimitTextField;
    private JComboBox ruleComboBox;

    /**
     * Constructor
     */
    public ABSimulationUI() {
        frame.setSize(800, 800);
        frame.setTitle("ABSimulation App");

        menuMgr.createDefaultActions(); // Set up default menu items

        SIMULATION.addObserver(CANVAS);

        showUI(); // Cause the Swing Dispatch thread to display the JFrame
    }

    // Create a north panel with buttons
    public JPanel getNorthPanel() {
        JPanel northPanel = new JPanel(); // Create a small canvas

        DesignGridLayout playOut = new DesignGridLayout(northPanel);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);

        boatPositionTextField = new JTextField("0,0");
        boatSpeedTextField = new JTextField("2");
        oceanContaminateStatusTextField = new JTextField(
                "20,30,50,10,40;" +
                "70,10,20,60,0;" +
                "20,30,50,10,40;" +
                "70,20,20,60,0;" +
                "10,90,20,10,0"
        );
        contaminateFlowRateTextField = new JTextField("0.05");
        simulationCycleLimitTextField = new JTextField("20");

        ruleComboBox = new JComboBox();
        ruleComboBox.addItem(new BFSABRule());
        ruleComboBox.addItem(new DFSABRule());
        ruleComboBox.addItem(new RandomABRule());

        playOut.row().grid(new JLabel("Rule")).add(ruleComboBox);
        playOut.emptyRow();
        playOut.row().grid(new JLabel("Boat initial Position")).add(boatPositionTextField);
        playOut.row().grid(new JLabel("Boat Speed")).add(boatSpeedTextField);
        playOut.row()
                .grid(new JLabel("<html>OceanContaminateStatus. Expect 2D integer array<br>Use semicolon to separate rows and<br>comma to seperate cell in a row</html>", SwingConstants.RIGHT))
                .add(oceanContaminateStatusTextField);
        playOut.row().grid(new JLabel("FlowRate in range: [0.0, 1.0]")).add(contaminateFlowRateTextField);
        playOut.row().grid(new JLabel("SimulationCycleLimit")).add(simulationCycleLimitTextField);
        playOut.row().center().add(startButton, stopButton);

        return northPanel;
    }

    /**
     * Create a center panel that has a drawable JPanel canvas
     */
    @Override
    public JPanel getCenterPanel() {
        return CANVAS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton eventButton = (JButton) e.getSource();

        if (eventButton == startButton) {
            // init ABRule
            SIMULATION.setRule((ABRule) ruleComboBox.getSelectedItem());

            // init Boat
            String[] positionInput = boatPositionTextField.getText().split(",");
            SIMULATION.setBoat(new Boat(
                    new Position(Integer.parseInt(positionInput[0]), Integer.parseInt(positionInput[1])),
                    Integer.parseInt(boatSpeedTextField.getText())
            ));

            // init OceanGrid
            List<List<Integer>> oceanContaminateStatus = Arrays.asList(oceanContaminateStatusTextField.getText().split(";"))
                    .stream()
                    .map(s -> Arrays.asList(s.split(",")).stream()
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            double flowRate = Double.parseDouble(contaminateFlowRateTextField.getText());
            SIMULATION.setOceanGrid(new OceanGrid(oceanContaminateStatus, flowRate));

            // init simulationCycleLimit
            SIMULATION.setSimulationCycleLimit(Integer.parseInt(simulationCycleLimitTextField.getText()));

            SIMULATION.start();
        } else if (eventButton == stopButton) {
            SIMULATION.stop();
        }
    }

    public static void main(String[] args) {
        new ABSimulationUI();
    }
}


