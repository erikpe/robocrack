package robocrack.gui.program;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.gui.GuiModel;
import robocrack.gui.common.SquareComponent;

@SuppressWarnings("serial")
public class InstructionSlotComponent extends SquareComponent implements
        Observer
{
    static final int WIDTH = 30;
    static final int HEIGHT = WIDTH;
    
    final InstructionPosition position;
    private final ProgramModel programModel;
    private final GuiModel guiModel;
    
    InstructionSlotComponent(final ProgramModel programModel, GuiModel guiModel,
            final InstructionPosition position)
    {
        this.programModel = programModel;
        this.guiModel = guiModel;
        this.position = position;
        
        programModel.addObserver(this);
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
    
    private boolean isActive()
    {
        return programModel.isActive(position);
    }
    
    @Override
    protected Color highlightColor(final Color color)
    {
        if (isActive())
        {
            return super.highlightColor(color);
        }
        
        return color;
    }
    
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
    }
    
    @Override
    protected Color getBackgroundColor()
    {
        if (!isActive())
        {
            return null;
        }
        
        switch (programModel.getCondition(position))
        {
        case ON_ALL: return Color.LIGHT_GRAY;
        case ON_RED: return Color.RED;
        case ON_BLUE: return Color.BLUE;
        case ON_GREEN: return Color.GREEN;
        }
        
        return null;
    }
    
    @Override
    protected Color getBorderColor()
    {
        if (isActive())
        {
            return super.getBorderColor();
        }
        
        return Color.LIGHT_GRAY;
    }
    
    @Override
    protected void leftButtonPressed()
    {
        if (!isActive())
        {
            return;
        }
        
        switch (guiModel.selectedFunctionButton())
        {
        case RED_BUTTON:
            programModel.setCondition(position, Condition.ON_RED);
            break;
            
        case GREEN_BUTTON:
            programModel.setCondition(position, Condition.ON_GREEN);
            break;
            
        case BLUE_BUTTON:
            programModel.setCondition(position, Condition.ON_BLUE);
            break;
            
        case NO_COLOR_BUTTON:
            programModel.setCondition(position, Condition.ON_ALL);
            break;
        }
    }
    
    @Override
    protected void rightButtonPressed()
    {
        if (!isActive())
        {
            return;
        }
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof InstructionPosition)
        {
            final InstructionPosition argPosition = (InstructionPosition) arg;
            if (argPosition.equals(position))
            {
                repaint();
            }
        }
        else if (arg == null)
        {
            repaint();
        }
    }
}
