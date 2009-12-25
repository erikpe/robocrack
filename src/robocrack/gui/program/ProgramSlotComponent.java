package robocrack.gui.program;

import robocrack.engine.program.ProgramModel;
import robocrack.gui.common.SquareComponent;

@SuppressWarnings("serial")
public class ProgramSlotComponent extends SquareComponent
{
    private static final int WIDTH = 30;
    private static final int HEIGHT = WIDTH;
    
    private final ProgramModel programModel;
    
    ProgramSlotComponent(final ProgramModel programModel)
    {
        this.programModel = programModel;
    }
    
    @Override
    protected int width()
    {
        return WIDTH;
    }

    @Override
    protected int height()
    {
        return HEIGHT;
    }
}
