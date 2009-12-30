package robocrack.gui.board;

import robocrack.engine.program.ProgramModel;
import robocrack.gui.GuiModel;
import robocrack.gui.GuiModel.BoardButton;
import robocrack.gui.common.ButtonPane;
import robocrack.gui.common.ToggleButton;

@SuppressWarnings("serial")
public class BoardButtonPane extends ButtonPane
{
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_SPACING = 3;
    
    public BoardButtonPane(final GuiModel guiModel,
            final ProgramModel programModel)
    {
        super(BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_SPACING);
        
        addButton(new ToggleButton(BoardButton.RED_BUTTON, guiModel, programModel));
        addButton(new ToggleButton(BoardButton.GREEN_BUTTON, guiModel, programModel));
        addButton(new ToggleButton(BoardButton.BLUE_BUTTON, guiModel, programModel));
        addButton(new ToggleButton(BoardButton.STAR_BUTTON, guiModel, programModel));
        addButton(new ToggleButton(BoardButton.ARROW_BUTTON, guiModel, programModel));
    }
}
