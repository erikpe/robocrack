package robocrack.gui.common;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ToggleButton<T extends Enum<?>> extends JToggleButton implements
        ActionListener, Observer
{
    private final T buttonEnum;
    private final Color color;
    private final GuiModel guiModel;
    
    public ToggleButton(final T buttonEnum, final String label,
            final Color color, final GuiModel guiModel)
    {
        super(label);
        
        this.buttonEnum = buttonEnum;
        this.color = color;
        this.guiModel = guiModel;

        initialize();
    }
    
    private void initialize()
    {
        if (color != null)
        {
            setBackground(color);
        }
        
        guiModel.addObserver(this);
        addActionListener(this);
        
        setSelected(guiModel.isSelected(buttonEnum));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiModel.selectButton(buttonEnum);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (buttonEnum == arg)
        {
            setSelected(guiModel.isSelected(buttonEnum));
        }
    }
}
