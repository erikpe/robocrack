package robocrack.gui.program;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import robocrack.engine.program.ProgramModel;
import robocrack.gui.GuiModel;
import robocrack.gui.program.PlusMinusButton.PlusMinus;

@SuppressWarnings("serial")
public class FunctionRowPane extends JComponent
{
    private static final int SPACING = 3;
    
    private final ProgramModel programModel;
    private final GuiModel guiModel;
    private final int function;
    
    public FunctionRowPane(final ProgramModel programModel,
            final GuiModel guiModel, final int function)
    {
        this.programModel = programModel;
        this.guiModel = guiModel;
        this.function = function;
        
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
    }
    
    private int addLabel(final int xBounds)
    {
        final JLabel label = new JLabel("F" + function);
        final int width = Math.max(25, label.getPreferredSize().width);
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
                guiModel, function);
        final int width = functionPane.getPreferredSize().width;
        final int height = functionPane.getPreferredSize().height;
        add(functionPane);
        
        functionPane.setBounds(xBounds, 0, width, height);
        
        return xBounds + width;
    }
    
    private int addPlusMinus(final int xBounds, final PlusMinus plusMinus)
    {
        final PlusMinusButton button = new PlusMinusButton(programModel,
                function, plusMinus);
        final int width = button.getPreferredSize().width;
        final int height = InstructionSlotComponent.HEIGHT;
        add(button);
        
        button.setBounds(xBounds, 0, width, height);
        
        return xBounds + width;
    }
}
