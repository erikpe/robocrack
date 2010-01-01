package robocrack.gui.program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;

@SuppressWarnings("serial")
public class PlusMinusButton extends JButton implements Observer, ActionListener
{
    static enum PlusMinus
    {
        PLUS("+"),
        MINUS("-");
        
        private final String string;
        
        private PlusMinus(final String string)
        {
            this.string = string;
        }
        
        @Override
        public String toString()
        {
            return string;
        }
    }
    
    private final ProgramModel programModel;
    private final int function;
    private final PlusMinus plusMinus;
    
    PlusMinusButton(final ProgramModel programModel, final int function,
            final PlusMinus plusMinus, final Simulator simulator)
    {
        super(plusMinus.toString());
        
        this.programModel = programModel;
        this.function = function;
        this.plusMinus = plusMinus;
        
        simulator.addObserver(this);
        addActionListener(this);
        
        update();
    }
    
    private void update()
    {
        if (programModel.isLocked())
        {
            setEnabled(false);
        }
        else if (plusMinus == PlusMinus.PLUS)
        {
            setEnabled(enablePlus());
        }
        else
        {
            setEnabled(enableMinus());
        }
    }
    
    private boolean enablePlus()
    {
        if (programModel.getFunctionLength(function)
                == programModel.getMaxFunctionLength())
        {
            return false;
        }
        else if (function == 1)
        {
            return true;
        }
        else if (programModel.getFunctionLength(function - 1) > 0)
        {
            return true;
        }
        
        return false;
    }
    
    private boolean enableMinus()
    {
        if (programModel.getFunctionLength(function) == 0)
        {
            return false;
        }
        else if (programModel.getFunctionLength(function) > 1)
        {
            return true;
        }
        else if (function == 1)
        {
            return false;
        }
        else if (function == programModel.getMaxFunctions())
        {
            return true;
        }
        else if (programModel.getFunctionLength(function + 1) > 0)
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        final int length = programModel.getFunctionLength(function);
        
        switch(plusMinus)
        {
        case PLUS:
            programModel.setFunctionLength(function, length + 1);
            break;
            
        case MINUS:
            programModel.setFunctionLength(function, length - 1);
            break;
        }
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
