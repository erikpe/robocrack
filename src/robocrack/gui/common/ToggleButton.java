package robocrack.gui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JToggleButton;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.OpCode;
import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class ToggleButton extends JToggleButton implements ActionListener,
        Observer
{
    private final Enum<?> buttonEnum;
    private final OpCode opCode;
    private final GuiModel guiModel;
    private final Simulator simulator;
    private final ProgramModel programModel;
    
    public ToggleButton(final Enum<?> buttonEnum, final OpCode opCode,
            final GuiModel guiModel, final Simulator simulator,
            final ProgramModel programModel)
    {
        super(buttonEnum.toString());
        
        this.buttonEnum = buttonEnum;
        this.opCode = opCode;
        this.guiModel = guiModel;
        this.simulator = simulator;
        this.programModel = programModel;

        initialize();
    }
    
    private void initialize()
    {
        guiModel.addObserver(this);
        simulator.addObserver(this);
        
        if (programModel != null)
        {
            programModel.addObserver(this);
        }
        
        addActionListener(this);
        
        update();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiModel.selectButton(buttonEnum);
    }
    
    private void update()
    {
        setSelected(guiModel.isSelected(buttonEnum));
        
        if (programModel != null && opCode != null)
        {
            setEnabled(programModel.isAllowed(opCode)
                    && simulator.getState() == SimulatorState.RESET);
        }
        else
        {
            setEnabled(simulator.getState() == SimulatorState.RESET);
        }
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (buttonEnum == arg || arg instanceof SimulatorState)
        {
            update();
        }
        else if (observable == programModel && arg == null)
        {
            update();
        }
    }
}
