package robocrack.gui.board;

import robocrack.gui.ButtonPane;
import robocrack.gui.GuiModel;
import robocrack.gui.ToggleButton;
import robocrack.gui.GuiModel.BoardButton;

@SuppressWarnings("serial")
public class BoardButtonPane extends ButtonPane<BoardButton>
{
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_SPACING = 3;
    
    public BoardButtonPane(final GuiModel guiModel)
    {
        super(BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_SPACING);
        initialize(guiModel);
    }
    
    private void initialize(final GuiModel guiModel)
    {
        addButton(new ToggleButton<BoardButton>(BoardButton.RED_BUTTON, "R",
                null, guiModel));
        addButton(new ToggleButton<BoardButton>(BoardButton.GREEN_BUTTON, "G",
                null, guiModel));
        addButton(new ToggleButton<BoardButton>(BoardButton.BLUE_BUTTON, "B",
                null, guiModel));
        addButton(new ToggleButton<BoardButton>(BoardButton.STAR_BUTTON, "S",
                null, guiModel));
        addButton(new ToggleButton<BoardButton>(BoardButton.ARROW_BUTTON, "A",
                null, guiModel));
    }
}
