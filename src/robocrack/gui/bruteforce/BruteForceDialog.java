package robocrack.gui.bruteforce;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import robocrack.engine.fastsimulator.FastSimulator;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;

@SuppressWarnings("serial")
public class BruteForceDialog extends JFrame
{
    private final BruteForcePane pane;
    
    public BruteForceDialog(final FastSimulator fastSim,
            final Simulator simulator, final ProgramModel programModel)
    {
        pane = new BruteForcePane(this, fastSim, simulator, programModel);
        
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(pane);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        pack();
        setMinimumSize(getPreferredSize());
    }
    
    public void start()
    {
        pane.start();
    }
}
