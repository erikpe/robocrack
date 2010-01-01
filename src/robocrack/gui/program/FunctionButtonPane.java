package robocrack.gui.program;

import robocrack.engine.simulator.Simulator;
import robocrack.gui.GuiModel;
import robocrack.gui.GuiModel.FunctionButton;
import robocrack.gui.common.ButtonPane;
import robocrack.gui.common.ToggleButton;

@SuppressWarnings("serial")
public class FunctionButtonPane extends ButtonPane
{
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_SPACING = 3;
    
    public FunctionButtonPane(final GuiModel guiModel, final Simulator simulator)
    {
        super(BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_SPACING);
        
        addButton(new ToggleButton(FunctionButton.FORWARD_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.LEFT_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.RIGHT_BUTTON, guiModel, simulator));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.F1_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.F2_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.F3_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.F4_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.F5_BUTTON, guiModel, simulator));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.PAINT_RED_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.PAINT_GREEN_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.PAINT_BLUE_BUTTON, guiModel, simulator));
        
        newRow();
        
        addButton(new ToggleButton(FunctionButton.RED_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.GREEN_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.BLUE_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.NO_COLOR_BUTTON, guiModel, simulator));
        addButton(new ToggleButton(FunctionButton.CLEAR_BUTTON, guiModel, simulator));
    }
}
