package robocrack.gui.common;

import java.awt.Dimension;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ButtonPane extends JComponent
{
    private final int buttonSpacing;

    private int xBounds = 0;
    private int yBounds = 0;

    private int rowHeight = 0;

    public ButtonPane(final int buttonSpacing)
    {
        this.buttonSpacing = buttonSpacing;
    }

    protected void addButton(final ToggleButton button)
    {
        add(button);

        final int buttonWidth = button.getPreferredSize().width;
        final int buttonHeight = button.getPreferredSize().height;

        button.setBounds(xBounds, yBounds, buttonWidth, buttonHeight);

        xBounds = xBounds + buttonWidth + buttonSpacing;
        rowHeight = Math.max(rowHeight, buttonHeight);

        setPreferredSize(new Dimension(xBounds + buttonWidth, yBounds
                + rowHeight));
    }

    protected void newRow()
    {
        xBounds = 0;
        yBounds = yBounds + rowHeight + buttonSpacing;
    }
}
