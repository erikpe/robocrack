package robocrack.gui.program;

import java.awt.Dimension;

import javax.swing.JComponent;

import robocrack.engine.program.ProgramModel;

@SuppressWarnings("serial")
public class ProgramPane extends JComponent
{
    private static final int SPACING = 3;
    
    private final ProgramModel programModel;
    
    public ProgramPane(final ProgramModel programModel)
    {
        this.programModel = programModel;
        initialize();
    }
    
    private void initialize()
    {
        int yBounds = 0;
        int maxWidth = 0;
        
        for (int i = 1; i <= programModel.getMaxFunctions(); ++i)
        {
            final FunctionRowPane functionRowPane = new FunctionRowPane(
                    programModel, i);
            
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
