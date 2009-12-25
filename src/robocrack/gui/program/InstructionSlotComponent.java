package robocrack.gui.program;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.gui.common.SquareComponent;

@SuppressWarnings("serial")
public class InstructionSlotComponent extends SquareComponent implements
        Observer
{
    static final int WIDTH = 30;
    static final int HEIGHT = WIDTH;
    
    final InstructionPosition position;
    private final ProgramModel programModel;
    
    InstructionSlotComponent(final ProgramModel programModel,
            final InstructionPosition position)
    {
        this.programModel = programModel;
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
        if (programModel.isActive(position))
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
        if (isActive())
        {
            return super.getBackgroundColor();
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
