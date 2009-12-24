package robotcrack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import robotcrack.gui.GuiState.BoardButton;

@SuppressWarnings("serial")
public class BoardButtonPaneOld extends JComponent implements Observer
{
    private static final int BUTTON_WIDTH = 30;
    private static final int BUTTON_HEIGHT = BUTTON_WIDTH;
    private static final int BUTTON_SPACING = 1;
    
    private final ButtonComponent<BoardButton> bRed;
    private final ButtonComponent<BoardButton> bGreen;
    private final ButtonComponent<BoardButton> bBlue;
    private final ButtonComponent<BoardButton> bStar;
    private final ButtonComponent<BoardButton> bArrow;
    
    private final List<ButtonComponent<BoardButton>> buttons;
    
    public BoardButtonPaneOld(final GuiState guiState)
    {
        bRed = new ButtonComponent<BoardButton>(Color.RED,
                BoardButton.RED_BUTTON, guiState);
        bGreen = new ButtonComponent<BoardButton>(Color.GREEN,
                BoardButton.GREEN_BUTTON, guiState);
        bBlue = new ButtonComponent<BoardButton>(Color.BLUE,
                BoardButton.BLUE_BUTTON, guiState);
        bStar = new ButtonComponent<BoardButton>(Color.LIGHT_GRAY,
                BoardButton.STAR_BUTTON, guiState);
        bArrow = new ButtonComponent<BoardButton>(Color.LIGHT_GRAY,
                BoardButton.ARROW_BUTTON, guiState);
        
        buttons = new ArrayList<ButtonComponent<BoardButton>>();
        
        buttons.add(bRed);
        buttons.add(bGreen);
        buttons.add(bBlue);
        buttons.add(bStar);
        buttons.add(bArrow);
        
        populate();
        
        guiState.addObserver(this);
    }
    
    private void populate()
    {
        for (int i = 0; i < buttons.size(); ++i)
        {
            final ButtonComponent<BoardButton> button = buttons.get(i);
            
            add(button);
            final int xBounds = i * (BUTTON_WIDTH + BUTTON_SPACING);
            button.setBounds(xBounds, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        
        final int width = buttons.size() * (BUTTON_WIDTH + BUTTON_SPACING)
                - BUTTON_SPACING;
        setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        repaint();
    }
}
