package robocrack.gui.program;

import robocrack.gui.GuiModel;
import robocrack.gui.GuiModel.FunctionButton;
import robocrack.gui.common.ButtonPane;
import robocrack.gui.common.ToggleButton;

@SuppressWarnings("serial")
public class FunctionButtonPane extends ButtonPane<FunctionButton>
{
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_SPACING = 3;
    
    public FunctionButtonPane(final GuiModel guiModel)
    {
        super(BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_SPACING);
        initialize(guiModel);
    }
    
    private void initialize(final GuiModel guiModel)
    {
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.FORWARD_BUTTON, "F", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.LEFT_BUTTON, "L", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.RIGHT_BUTTON, "R", null, guiModel));
        
        newRow();
        
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.F1_BUTTON, "F1", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.F2_BUTTON, "F2", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.F3_BUTTON, "F3", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.F4_BUTTON, "F4", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.F5_BUTTON, "F5", null, guiModel));
        
        newRow();
        
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.RED_BUTTON, "R", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.GREEN_BUTTON, "G", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.BLUE_BUTTON, "B", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.NO_COLOR_BUTTON, "NC", null, guiModel));
        addButton(new ToggleButton<FunctionButton>(
                FunctionButton.CLEAR_BUTTON, "C", null, guiModel));
    }
}
