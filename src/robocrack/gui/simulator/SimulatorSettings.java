package robocrack.gui.simulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;

@SuppressWarnings("serial")
public class SimulatorSettings extends JComponent implements ActionListener, Observer
{
    private final Simulator simulator;
    private final JCheckBox tailCallBox;
    
    private int preferredWidth = 0;
    
    public SimulatorSettings(final Simulator simulator)
    {
        this.simulator = simulator;
        this.tailCallBox = new JCheckBox("Enable tail call optimization");
        
        initialize();
    }
    
    private void initialize()
    {
        simulator.addObserver(this);
        
        int yBounds = addBox(tailCallBox, 0);
        setPreferredSize(new Dimension(preferredWidth, yBounds));
        
        update();
    }
    
    private int addBox(final JCheckBox box, final int yBounds)
    {
        box.addActionListener(this);
        add(box);
        
        final int bWidth = box.getPreferredSize().width;
        final int bHeight = box.getPreferredSize().height;
        
        box.setBounds(0, yBounds, bWidth, bHeight);
        
        preferredWidth = Math.max(preferredWidth, bWidth);
        
        return yBounds + bHeight;
    }
    
    private void update()
    {
        tailCallBox.setSelected(simulator.getTailCallOptimization());
        tailCallBox
                .setEnabled(simulator.getState() != SimulatorState.BRUTE_FORCING);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        update();
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == tailCallBox)
        {
            simulator.setTailCallOptimization(tailCallBox.isSelected());
        }
    }
}
