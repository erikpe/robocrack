package robocrack.gui.simulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;

import robocrack.engine.fastsimulator.FastSimulator;
import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;

@SuppressWarnings("serial")
public class SimulatorButtonPane extends JComponent implements ActionListener,
        Observer
{
    private static final int SPACING = 3;
    
    private final Simulator simulator;
    
    private final JButton playPauseButton;
    private final JButton stepButton;
    private final JButton resetButton;
    private final JButton bruteForce;
    
    public SimulatorButtonPane(final Simulator simulator)
    {
        this.simulator = simulator;
        
        this.playPauseButton = new JButton("Play/Pause");
        this.stepButton = new JButton("Step");
        this.resetButton = new JButton("Reset");
        this.bruteForce = new JButton("Brute Force");

        initialize();
    }
    
    private void initialize()
    {
        int xBounds = 0;
        
        xBounds = addButton(playPauseButton, xBounds);
        xBounds = addButton(stepButton, xBounds + SPACING);
        xBounds = addButton(resetButton, xBounds + SPACING);
        xBounds = addButton(bruteForce, xBounds + SPACING);
        
        final int height = playPauseButton.getPreferredSize().height;
        setPreferredSize(new Dimension(xBounds, height));
        
        playPauseButton.addActionListener(this);
        stepButton.addActionListener(this);
        resetButton.addActionListener(this);
        bruteForce.addActionListener(this);
        
        update();
        
        simulator.addObserver(this);
    }
    
    private int addButton(final JButton button, final int xBounds)
    {
        add(button);
        
        final int width = button.getPreferredSize().width;
        final int height = button.getPreferredSize().height;
        
        button.setBounds(xBounds, 0, width, height);
        
        return xBounds + width;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == playPauseButton)
        {
            simulator.getRunner().playPause();
        }
        if (e.getSource() == stepButton)
        {
            simulator.getRunner().step();
        }
        else if (e.getSource() == resetButton)
        {
            simulator.getRunner().reset();
        }
        else if (e.getSource() == bruteForce)
        {
            simulator.bruteForce();
        }
    }
    
    private void update()
    {
        playPauseButton.setEnabled(simulator.getState() != SimulatorState.HALTED);
        stepButton.setEnabled(simulator.getState() != SimulatorState.HALTED);
        resetButton.setEnabled(simulator.getState() != SimulatorState.RESET);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof SimulatorState)
        {
            update();
        }
    }
}
