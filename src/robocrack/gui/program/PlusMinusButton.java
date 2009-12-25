package robocrack.gui.program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import robocrack.engine.program.ProgramModel;

@SuppressWarnings("serial")
public class PlusMinusButton extends JButton implements Observer, ActionListener
{
    static enum PlusMinus
    {
        PLUS,
        MINUS
    }
    
    private final ProgramModel programModel;
    private final int function;
    private final PlusMinus plusMinus;
    
    PlusMinusButton(final ProgramModel programModel, final int function,
            final PlusMinus plusMinus)
    {
        super(plusMinus == PlusMinus.PLUS ? "+" : "-");
        
        this.programModel = programModel;
        this.function = function;
        this.plusMinus = plusMinus;
        
        programModel.addObserver(this);
        addActionListener(this);
        
        update();
    }
    
    private void update()
    {
        switch(plusMinus)
        {
        case PLUS:
            setEnabled(enablePlus());
            break;
            
        case MINUS:
            setEnabled(enableMinus());
            break;
        }
    }
    
    private boolean enablePlus()
    {
        if (programModel.getFunctionLength(function) == programModel.getMaxFunctionLength())
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
    public void update(final Observable observable, final Object arg)
    {
        update();
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
}
