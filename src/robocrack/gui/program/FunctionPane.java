package robocrack.gui.program;

import java.awt.Dimension;

import javax.swing.JComponent;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;

@SuppressWarnings("serial")
public class FunctionPane extends JComponent
{
    private static final int SPACING = 3;
    
    private final ProgramModel programModel;
    private final int function;
    
    private final InstructionSlotComponent[] instructions;
    
    public FunctionPane(final ProgramModel programModel, final int function)
    {
        this.programModel = programModel;
        this.function = function;
        this.instructions = new InstructionSlotComponent[programModel
                .getMaxFunctionLength()];
        
        initialize();
    }
    
    private void initialize()
    {
        int xBounds = 0;
        
        for (int i = 0; i < instructions.length; ++i)
        {
            final InstructionPosition position = InstructionPosition.make(
                    function, i);
            
            instructions[i] = new InstructionSlotComponent(programModel,
                    position);
            add(instructions[i]);
            
            instructions[i].setBounds(xBounds, 0,
                    InstructionSlotComponent.WIDTH,
                    InstructionSlotComponent.HEIGHT);
            xBounds = xBounds + InstructionSlotComponent.WIDTH + SPACING;
        }
        
        final int width = xBounds - SPACING;
        setPreferredSize(new Dimension(width, InstructionSlotComponent.HEIGHT));
    }
}
