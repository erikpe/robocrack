package robocrack.gui;

import java.awt.Dimension;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ButtonPane<T extends Enum<?>> extends JComponent
{
    private final int buttonWidth;
    private final int buttonHeight;
    private final int buttonSpacing;
    
    private int row = 0;
    private int column = 0;
    
    ButtonPane(final int buttonWidth, final int buttonHeight, final int buttonSpacing)
    {
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.buttonSpacing = buttonSpacing;
        
        setPreferredSize(new Dimension(0, 0));
    }
    
    protected void addButton(final ToggleButton<T> button)
    {
        add(button);
        final int xBounds = column * (buttonWidth + buttonSpacing);
        final int yBounds = row * (buttonHeight + buttonSpacing);
        button.setBounds(xBounds, yBounds, buttonWidth, buttonHeight);
        
        final int width = (column + 1) * (buttonWidth + buttonSpacing)
                - buttonSpacing;
        final int height = (row + 1) * (buttonHeight + buttonSpacing)
                - buttonSpacing;
        
        setPreferredSize(new Dimension(width, height));
        
        column++;
    }
    
    protected void newRow()
    {
        column = 0;
        row++;
    }
}
