package robocrack.gui.simulator;

import java.awt.Dimension;

import javax.swing.JComponent;

import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.StackDepth;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class StackPane extends JComponent
{
    private static final int SPACING = 3;
    
    public StackPane(final Simulator simulator, final GuiModel guiModel,
            final int width, final int height)
    {
        initialize(simulator, guiModel, width, height);
    }
    
    private void initialize(final Simulator simulator, final GuiModel guiModel,
            final int width, final int height)
    {
        int stackDepth = 0;
        
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                final StackComponent stackComponent = new StackComponent(
                        simulator, guiModel, new StackDepth(stackDepth));
                add(stackComponent);
                
                final int xBounds = x * (StackComponent.WIDTH + SPACING);
                final int yBounds = y * (StackComponent.HEIGHT + SPACING);
                
                stackComponent.setBounds(xBounds, yBounds,
                        StackComponent.WIDTH, StackComponent.HEIGHT);
                
                stackDepth++;
            }
        }
        
        final int paneWidth = width * (StackComponent.WIDTH + SPACING)
                - SPACING;
        final int paneHeight = height * (StackComponent.HEIGHT + SPACING)
                - SPACING;
        
        setPreferredSize(new Dimension(paneWidth, paneHeight));
    }
}
