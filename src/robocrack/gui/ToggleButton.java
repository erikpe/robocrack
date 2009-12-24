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
    private final GuiModel guiModel;
    
    ToggleButton(final Color color, final T buttonEnum, final GuiModel guiState)
    {
        this.color = color;
        this.buttonEnum = buttonEnum;
        this.guiModel = guiState;
        
        initialize();
    }
    
    private void initialize()
    {
        guiModel.addObserver(this);
        addActionListener(this);
        
        setBackground(color);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        setSelected(guiModel.isSelected(buttonEnum));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiModel.selectButton(buttonEnum);
    }
}
