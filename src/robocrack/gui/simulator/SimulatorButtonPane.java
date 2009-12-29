package robocrack.gui.simulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import robocrack.engine.simulator.Simulator;

@SuppressWarnings("serial")
public class SimulatorButtonPane extends JComponent implements ActionListener
{
    private static final int SPACING = 3;
    
    private final Simulator simulator;
    
    private final JButton playPauseButton;
    private final JButton stepButton;
    private final JButton resetButton;
    
    public SimulatorButtonPane(final Simulator simulator)
    {
        this.simulator = simulator;
        
        this.playPauseButton = new JButton("Play/Pause");
        this.stepButton = new JButton("Step");
        this.resetButton = new JButton("Reset");

        initialize();
    }
    
    private void initialize()
    {
        int xBounds = 0;
        
        xBounds = addButton(playPauseButton, xBounds);
        xBounds = addButton(stepButton, xBounds + SPACING);
        xBounds = addButton(resetButton, xBounds + SPACING);
        
        final int height = playPauseButton.getPreferredSize().height;
        setPreferredSize(new Dimension(xBounds, height));
        
        stepButton.addActionListener(this);
        resetButton.addActionListener(this);
        
        playPauseButton.setEnabled(false);
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
        if (e.getSource() == stepButton)
        {
            simulator.step();
        }
        else if (e.getSource() == resetButton)
        {
            simulator.reset();
        }
    }
}
