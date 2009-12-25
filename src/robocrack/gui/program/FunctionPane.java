package robocrack.gui.program;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;

@SuppressWarnings("serial")
public class FunctionPane extends JComponent implements Observer
{
    private final ProgramModel programModel;
    private final int function;
    
    //private final InstructionComponent[] instructions;
    
    public FunctionPane(final ProgramModel programModel, final int function)
    {
        this.programModel = programModel;
        this.function = function;
        
        programModel.addObserver(this);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof InstructionPosition)
        {
            final InstructionPosition position = (InstructionPosition) arg;
            
            if (position.function == function)
            {
                //instructions[position.slot].repaint();
            }
        }
    }
}
