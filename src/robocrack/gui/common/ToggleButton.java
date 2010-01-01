package robocrack.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ToggleButton extends JToggleButton implements ActionListener,
        Observer
{
    private final Enum<?> buttonEnum;
    private final GuiModel guiModel;
    private final Simulator simulator;
    
    public ToggleButton(final Enum<?> buttonEnum, final GuiModel guiModel,
            final Simulator simulator)
    {
        super(buttonEnum.toString());
        
        this.buttonEnum = buttonEnum;
        this.guiModel = guiModel;
        this.simulator = simulator;

        initialize();
    }
    
    private void initialize()
    {
        guiModel.addObserver(this);
        simulator.addObserver(this);
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
        setEnabled(simulator.getState() == SimulatorState.RESET);
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
