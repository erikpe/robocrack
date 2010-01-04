package robocrack.gui.simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import robocrack.engine.simulator.Simulator;

@SuppressWarnings("serial")
public class SimulatorSettings extends JComponent implements ActionListener, Observer
{
    final Simulator simulator;
    final JCheckBox tailCallButton;
    
    public SimulatorSettings(final Simulator simulator)
    {
        this.simulator = simulator;
        this.tailCallButton = new JCheckBox("Enable tail call optimization");
        
        initialize();
    }
    
    private void initialize()
    {
        simulator.addObserver(this);
        tailCallButton.addActionListener(this);
        
        add(tailCallButton);
        
        final int bWidth = tailCallButton.getPreferredSize().width;
        final int bHeight = tailCallButton.getPreferredSize().height;
        
        tailCallButton.setBounds(0, 0, bWidth, bHeight);
        
        setPreferredSize(tailCallButton.getPreferredSize());
        
        update();
    }
    
    private void update()
    {
        tailCallButton.setSelected(simulator.getTailCallOptimization());
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg == null)
        {
            update();
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == tailCallButton)
        {
            simulator.setTailCallOptimization(tailCallButton.isSelected());
        }
    }
}
