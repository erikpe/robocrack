package robocrack.gui.bruteforce;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import robocrack.engine.fastsimulator.FastSimulator;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;

@SuppressWarnings("serial")
public class BruteForceDialog extends JFrame
{
    private final BruteForcePane pane;
    protected final JTextArea textArea;
    private final JScrollPane scrollPane;
    
    public BruteForceDialog(final FastSimulator fastSim,
            final Simulator simulator, final ProgramModel programModel)
    {
        this.textArea = new JTextArea();
        this.scrollPane = new JScrollPane(textArea);
        this.pane = new BruteForcePane(this, fastSim, simulator, programModel);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(pane, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        pack();
        setSize(new Dimension(600, 300));
    }
    
    public void start()
    {
        pane.start();
    }
}
