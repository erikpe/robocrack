package robocrack.gui.program;

import java.awt.Dimension;

import javax.swing.JComponent;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ProgramPane extends JComponent
{
    private static final int SPACING = 3;

    public ProgramPane(final ProgramModel programModel, final GuiModel guiModel,
            final Simulator simulator)
    {
        int yBounds = 0;
        int maxWidth = 0;

        for (int func = 1; func <= programModel.getMaxFunctions(); ++func)
        {
            final FunctionRowPane functionRowPane = new FunctionRowPane(
                    programModel, guiModel, func, simulator);

            final int width = functionRowPane.getPreferredSize().width;
            final int height = functionRowPane.getPreferredSize().height;

            functionRowPane.setBounds(0, yBounds, width, height);
            add(functionRowPane);

            maxWidth = Math.max(maxWidth, width);
            yBounds = yBounds + height + SPACING;
        }

        setPreferredSize(new Dimension(maxWidth, yBounds - SPACING));
    }
}
