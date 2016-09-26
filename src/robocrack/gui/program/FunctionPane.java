package robocrack.gui.program;

import java.awt.Dimension;

import javax.swing.JComponent;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class FunctionPane extends JComponent
{
    private static final int SPACING = 3;

    public FunctionPane(final ProgramModel programModel, final GuiModel guiModel,
            final int function, final Simulator simulator)
    {
        int xBounds = 0;

        for (int i = 0; i < programModel.getMaxFunctionLength(); ++i)
        {
            final InstructionPosition position = InstructionPosition.make(
                    function, i);
            final InstructionSlotComponent instruction = new InstructionSlotComponent(
                    programModel, guiModel, position, simulator);
            add(instruction);

            instruction.setBounds(xBounds, 0, InstructionSlotComponent.WIDTH,
                    InstructionSlotComponent.HEIGHT);
            xBounds = xBounds + InstructionSlotComponent.WIDTH + SPACING;
        }

        final int width = xBounds - SPACING;
        setPreferredSize(new Dimension(width, InstructionSlotComponent.HEIGHT));
    }
}
