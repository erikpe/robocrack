package robocrack.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class ToggleButton<T extends Enum<?>> extends JToggleButton implements
        ActionListener, Observer
{
    private final Color color;
    private final T buttonEnum;
    private final GuiState guiState;
    
    ToggleButton(final Color color, final T buttonEnum, final GuiState guiState)
    {
        this.color = color;
        this.buttonEnum = buttonEnum;
        this.guiState = guiState;
        
        initialize();
    }
    
    private void initialize()
    {
        guiState.addObserver(this);
        addActionListener(this);
        
        setBackground(color);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        setSelected(guiState.isSelected(buttonEnum));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiState.selectButton(buttonEnum);
    }
}
