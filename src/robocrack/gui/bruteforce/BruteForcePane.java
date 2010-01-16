package robocrack.gui.bruteforce;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import robocrack.engine.fastsimulator.FastSimulator;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;

@SuppressWarnings("serial")
public class BruteForcePane extends JComponent implements Observer, ActionListener
{
    private static final int SPACING = 3;
    
    private static final int UPDATE_INTERVAL_MSEC = 500;
    private static final int TEXT_AREA_WIDTH = 600;
    private static final int TEXT_AREA_HEIGHT = 250;
    
    private final JFrame frame;
    private final FastSimulator fastSim;
    private final Simulator simulator;
    private final ProgramModel programModel;
    
    private final JTextArea textArea;
    private final JButton cancelButton;
    private final JButton loadButton;
    
    private Thread simThread;
    private Timer updateTimer;
    
    private long startTime;
    
    public BruteForcePane(final JFrame frame, final FastSimulator fastSim,
            final Simulator simulator, final ProgramModel programModel)
    {
        this.frame = frame;
        this.fastSim = fastSim;
        this.simulator = simulator;
        this.programModel = programModel;
        
        this.textArea = new JTextArea();
        this.cancelButton = new JButton("Cancel");
        this.loadButton = new JButton("Load solution");
        
        initialize();
    }
    
    private void initialize()
    {
        frame.setTitle("Brute forcing ...");
        
        textArea.setEditable(false);
        loadButton.setEnabled(false);
        
        add(textArea);
        textArea.setBounds(0, 0, TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT);
        
        int xBounds = 0;
        int yBounds = TEXT_AREA_HEIGHT + SPACING;
        
        xBounds = addComponent(cancelButton, xBounds, yBounds);
        xBounds = addComponent(loadButton, xBounds + SPACING, yBounds);
        
        final int width = Math.max(textArea.getWidth(), xBounds);
        final int height = textArea.getHeight() + SPACING
                + cancelButton.getHeight();
        
        setPreferredSize(new Dimension(width, height));
        
        cancelButton.addActionListener(this);
        loadButton.addActionListener(this);
        
        fastSim.addObserver(this);
    }
    
    private int addComponent(final JComponent comp, final int xBounds,
            final int yBounds)
    {
        final int width = comp.getPreferredSize().width;
        final int height = comp.getPreferredSize().height;
        
        add(comp);
        comp.setBounds(xBounds, yBounds, width, height);
        
        return xBounds + width;
    }
    
    public void start()
    {
        simThread = new Thread()
        {
            @Override
            public void run()
            {
                fastSim.bruteForce();
            }
        };
        
        startTime = System.currentTimeMillis();
        simThread.start();
        
        final TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                update();
            }
        };
        
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(timerTask, 0, UPDATE_INTERVAL_MSEC);
    }
    
    private void update()
    {
        final long now = System.currentTimeMillis();
        final double time = (now - startTime) / 1000.0;
        
        final long testedPrograms = fastSim.totTestedPrograms;
        final long simsteps = fastSim.totSimsteps;
        
        final StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb);
        
        final double amountTested = (100.0 * testedPrograms)
                / fastSim.totPrograms.doubleValue();
        
        formatter.format("Time used: %.1f sec\n", time);
        formatter.format("Programs tested: %d (%.4f%%)\n", testedPrograms, amountTested);
        formatter.format("Progs/s: %d\n", (long) (testedPrograms / time));
        formatter.format("Simulation steps: %d\n", simsteps);
        formatter.format("Steps/s: %d\n", (long) (simsteps / time));
        formatter.format("Avg steps/prog: %.2f\n", ((float) simsteps) /testedPrograms);
        
        textArea.setText(sb.toString());
    }
    
    private void printSolution(final Instruction[][] solution)
    {
        final StringBuilder sb = new StringBuilder(textArea.getText());
        
        sb.append("\nSolution: \n");
        
        int funIdx = 1;
        
        for (final Instruction[] fun : solution)
        {
            sb.append("F").append(funIdx).append(": ");
            
            for (final Instruction inst : fun)
            {
                sb.append(inst).append(" ");
            }
            
            sb.append('\n');
            
            funIdx++;
        }
        
        textArea.setText(sb.toString());

    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        updateTimer.cancel();
        update();
        
        if (fastSim.program != null)
        {
            printSolution(fastSim.program);
            frame.setTitle("Solution found!");
            loadButton.setEnabled(true);
        }
        else
        {
            frame.setTitle("No solution found");
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == cancelButton)
        {
            updateTimer.cancel();
            fastSim.stop = true;
            simulator.isBruteForcing(false);
            frame.dispose();
        }
        else if (e.getSource() == loadButton)
        {
            final Instruction[][] solution = fastSim.program;
            
            simulator.isBruteForcing(false);
            for (int func = 0; func < solution.length; ++func)
            {
                for (int slot = 0; slot < solution[func].length; ++slot)
                {
                    final InstructionPosition pos = InstructionPosition.make(
                            func + 1, slot);
                    
                    programModel.setCondition(pos, solution[func][slot].condition);
                    programModel.setOpCode(pos, solution[func][slot].opCode);
                }
            }
            simulator.isBruteForcing(true);
        }
    }
}
