package robocrack.gui.program;

import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.OpCode;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.GuiModel;
import robocrack.gui.GuiModel.FunctionButton;
import robocrack.gui.common.ButtonPane;
import robocrack.gui.common.ToggleButton;

@SuppressWarnings("serial")
public class FunctionButtonPane extends ButtonPane
{
    private static final int BUTTON_SPACING = 3;
    
    public FunctionButtonPane(final GuiModel guiModel,
            final Simulator simulator, final ProgramModel programModel)
    {
        super(BUTTON_SPACING);
        
        addButton(new ToggleButton(FunctionButton.FORWARD_BUTTON, OpCode.GO_FORWARD, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.LEFT_BUTTON, OpCode.TURN_LEFT, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.RIGHT_BUTTON, OpCode.TURN_RIGHT, guiModel, simulator, programModel));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.F1_BUTTON, OpCode.CALL_F1, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.F2_BUTTON, OpCode.CALL_F2, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.F3_BUTTON, OpCode.CALL_F3, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.F4_BUTTON, OpCode.CALL_F4, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.F5_BUTTON, OpCode.CALL_F5, guiModel, simulator, programModel));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.PAINT_RED_BUTTON, OpCode.PAINT_RED, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.PAINT_GREEN_BUTTON, OpCode.PAINT_GREEN, guiModel, simulator, programModel));
        addButton(new ToggleButton(FunctionButton.PAINT_BLUE_BUTTON, OpCode.PAINT_BLUE, guiModel, simulator, programModel));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.RED_BUTTON, null, guiModel, simulator, null));
        addButton(new ToggleButton(FunctionButton.GREEN_BUTTON, null, guiModel, simulator, null));
        addButton(new ToggleButton(FunctionButton.BLUE_BUTTON, null, guiModel, simulator, null));
        addButton(new ToggleButton(FunctionButton.NO_COLOR_BUTTON, null, guiModel, simulator, null));
        addButton(new ToggleButton(FunctionButton.CLEAR_BUTTON, null, guiModel, simulator, null));
    }
}
