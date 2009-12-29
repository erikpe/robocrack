package robocrack.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ToggleButton extends JToggleButton implements ActionListener,
        Observer
{
    private final Enum<?> buttonEnum;
    private final GuiModel guiModel;
    
    public ToggleButton(final Enum<?> buttonEnum, final GuiModel guiModel)
    {
        super(buttonEnum.toString());
        
        this.buttonEnum = buttonEnum;
        this.guiModel = guiModel;

        initialize();
    }
    
    private void initialize()
    {
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
