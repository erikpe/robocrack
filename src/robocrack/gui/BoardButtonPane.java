package robocrack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import robocrack.gui.GuiState.BoardButton;

public class BoardButtonPane extends JComponent
{
    private static final int BUTTON_WIDTH = 30;
    private static final int BUTTON_HEIGHT = BUTTON_WIDTH;
    private static final int BUTTON_SPACING = 1;
    
    private final GuiState guiState;
    
    private final ToggleButton<BoardButton> bRed;
    private final ToggleButton<BoardButton> bGreen;
    private final ToggleButton<BoardButton> bBlue;
    private final ToggleButton<BoardButton> bStar;
    private final ToggleButton<BoardButton> bArrow;
    
    private final List<ToggleButton<BoardButton>> buttons;
    
    BoardButtonPane(final GuiState guiState)
    {
        this.guiState = guiState;
        
        bRed = new ToggleButton<BoardButton>(Color.RED, BoardButton.RED_BUTTON, guiState);
        bGreen = new ToggleButton<BoardButton>(Color.GREEN, BoardButton.GREEN_BUTTON, guiState);
        bBlue = new ToggleButton<BoardButton>(Color.BLUE, BoardButton.BLUE_BUTTON, guiState);
        bStar = new ToggleButton<BoardButton>(Color.LIGHT_GRAY, BoardButton.STAR_BUTTON, guiState);
        bArrow = new ToggleButton<BoardButton>(Color.LIGHT_GRAY, BoardButton.ARROW_BUTTON, guiState);
        
        buttons = new ArrayList<ToggleButton<BoardButton>>();
        buttons.add(bRed);
        buttons.add(bGreen);
        buttons.add(bBlue);
        buttons.add(bStar);
        buttons.add(bArrow);
        
        populate();
    }
    
    private void populate()
    {
        for (int i = 0; i < buttons.size(); ++i)
        {
            final ToggleButton<BoardButton> button = buttons.get(i);
            
            add(button);
            final int xBounds = i * (BUTTON_WIDTH + BUTTON_SPACING);
            button.setBounds(xBounds, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        
        final int width = buttons.size() * (BUTTON_WIDTH + BUTTON_SPACING)
                - BUTTON_SPACING;
        setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
    }
}
