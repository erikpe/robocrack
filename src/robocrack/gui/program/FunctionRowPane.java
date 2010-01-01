package robocrack.gui.program;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.GuiModel;
import robocrack.gui.program.PlusMinusButton.PlusMinus;

@SuppressWarnings("serial")
public class FunctionRowPane extends JComponent implements Observer
{
    private static final int SPACING = 3;
    
    private final ProgramModel programModel;
    private final GuiModel guiModel;
    private final int function;
    private final Simulator simulator;
    private final JLabel label;
    
    public FunctionRowPane(final ProgramModel programModel,
            final GuiModel guiModel, final int function, final Simulator simulator)
    {
        this.programModel = programModel;
        this.guiModel = guiModel;
        this.function = function;
        this.simulator = simulator;
        this.label = new JLabel();
        
        initialize();
    }
    
    private void initialize()
    {
        int xBounds = 0;
        
        xBounds = addLabel(xBounds + SPACING);
        xBounds = addFunction(xBounds + SPACING);
        xBounds = addPlusMinus(xBounds + 4 * SPACING, PlusMinus.MINUS);
        xBounds = addPlusMinus(xBounds + SPACING, PlusMinus.PLUS);
        
        setPreferredSize(new Dimension(xBounds, InstructionSlotComponent.HEIGHT));
        
        programModel.addObserver(this);
    }
    
    private int addLabel(final int xBounds)
    {
        updateLabel();
        
        final int width = 50;
        final int height = label.getPreferredSize().height;
        add(label);

        int yBounds = Math.max(0,
                (InstructionSlotComponent.HEIGHT - height) / 2);
        label.setBounds(xBounds, yBounds, width, height);
        
        return xBounds + width;
    }
    
    private int addFunction(final int xBounds)
    {
        final FunctionPane functionPane = new FunctionPane(programModel,
                guiModel, function, simulator);
        final int width = functionPane.getPreferredSize().width;
        final int height = functionPane.getPreferredSize().height;
        add(functionPane);
        
        functionPane.setBounds(xBounds, 0, width, height);
        
        return xBounds + width;
    }
    
    private int addPlusMinus(final int xBounds, final PlusMinus plusMinus)
    {
        final PlusMinusButton button = new PlusMinusButton(programModel,
                function, plusMinus, simulator);
        final int width = button.getPreferredSize().width;
        final int height = InstructionSlotComponent.HEIGHT;
        add(button);
        
        button.setBounds(xBounds, 0, width, height);
        
        return xBounds + width;
    }
    
    private void updateLabel()
    {
        final int length = programModel.getFunctionLength(function);
        String text = "F" + function;
        
        if (length == 0)
        {
            label.setForeground(Color.GRAY);
        }
        else
        {
            label.setForeground(Color.BLACK);
            text = text + " (" + length + ")";
        }
        
        label.setText(text);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof InstructionPosition)
        {
            final InstructionPosition argPosition = (InstructionPosition) arg;
            if (argPosition.function == function)
            {
                updateLabel();
            }
        }
    }
}
