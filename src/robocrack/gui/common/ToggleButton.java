package robocrack.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator.SimulatorState;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ToggleButton extends JToggleButton implements ActionListener,
        Observer
{
    private final Enum<?> buttonEnum;
    private final GuiModel guiModel;
    private final ProgramModel programModel;
    
    public ToggleButton(final Enum<?> buttonEnum, final GuiModel guiModel,
            final ProgramModel programModel)
    {
        super(buttonEnum.toString());
        
        this.buttonEnum = buttonEnum;
        this.guiModel = guiModel;
        this.programModel = programModel;

        initialize();
    }
    
    private void initialize()
    {
        guiModel.addObserver(this);
        programModel.addObserver(this);
        addActionListener(this);
        
        setSelected(guiModel.isSelected(buttonEnum));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiModel.selectButton(buttonEnum);
    }
    
    private void update()
    {
        setSelected(guiModel.isSelected(buttonEnum));
        setEnabled(!programModel.isLocked());
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (buttonEnum == arg || arg instanceof SimulatorState)
        {
            update();
        }
    }
}
