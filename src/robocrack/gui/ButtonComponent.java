package robocrack.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class ButtonComponent<T extends Enum<?>> extends SquareComponent
        implements Observer
{
    private final static int WIDTH = 30;
    private final static int HEIGHT = WIDTH;
    
    private final Color color;
    private final T buttonEnum;
    private final GuiModel guiModel;
    
    ButtonComponent(final Color color, final T buttonEnum,
            final GuiModel guiState)
    {
        this.color = color;
        this.buttonEnum = buttonEnum;
        this.guiModel = guiState;
    }
    
    @Override
    protected Color getBackgroundColor()
    {
        return color;
    }
    
    @Override
    protected int height()
    {
        return WIDTH;
    }
    
    @Override
    protected int width()
    {
        return HEIGHT;
    }
    
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        paintIcon(g);
        paintSelection(g);
    }
    
    protected void paintIcon(final Graphics g) { }
    
    private void paintSelection(final Graphics g)
    {
        if (!guiModel.isSelected(buttonEnum))
        {
            return;
        }
        
        final int x = width() / 8;
        final int y = height() / 8;
        final int circleWidth = width() - 2 * x;
        final int circleHeight = height() - 2 * y;
        
        g.setColor(Color.BLACK);
        g.drawOval(x, y, circleWidth, circleHeight);
        g.setColor(Color.WHITE);
        g.drawOval(x + 1, y + 1, circleWidth - 2, circleHeight - 2);
    }
    
    @Override
    void leftButtonPressed()
    {
        guiModel.selectButton(buttonEnum);
    }
    
    @Override
    public void update(final Observable oservable, final Object arg)
    {
        if (arg == buttonEnum)
        {
            repaint();
        }
    }
}
