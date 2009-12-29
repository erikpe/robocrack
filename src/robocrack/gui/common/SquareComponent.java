package robocrack.gui.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class SquareComponent extends JComponent implements
        MouseListener
{
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final Color DEFAULT_INACTIVE_COLOR = null;
    private static final Color DEFAULT_INACTIVE_BORDER_COLOR = Color.LIGHT_GRAY;
    
    private boolean isHighlighted = false;
    
    public SquareComponent()
    {
        addMouseListener(this);
    }
    
    protected boolean isActive()
    {
        return true;
    }
    
    protected boolean isLocked()
    {
        return true;
    }
    
    protected abstract int width();
    
    protected abstract int height();
    
    protected Color getBackgroundColor()
    {
        return DEFAULT_COLOR;
    }
    
    protected Color getBorderColor()
    {
        return DEFAULT_BORDER_COLOR;
    }
    
    protected Color getInactiveBackgroundColor()
    {
        return DEFAULT_INACTIVE_COLOR;
    }
    
    protected Color getInactiveBorderColor()
    {
        return DEFAULT_INACTIVE_BORDER_COLOR;
    }
    
    protected Color highlightColor(final Color color)
    {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        red = red + 2 * (255 - red) / 3;
        green = green + 2 * (255 - green) / 3;
        blue = blue + 2 * (255 - blue) / 3;
        
        return new Color(red, green, blue);
    }
    
    protected void paintBackground(final Graphics g)
    {
        final Color color;
        
        if (!isActive())
        {
            color = getInactiveBackgroundColor();
        }
        else if (isHighlighted)
        {
            color = highlightColor(getBackgroundColor());
        }
        else
        {
            color = getBackgroundColor();
        }
        
        if (color != null)
        {
            g.setColor(color);
            g.fillRect(0, 0, width() - 1, height() - 1);
        }
    }
    
    protected void paintBorder(final Graphics g)
    {
        final Color color;
        
        if (!isActive())
        {
            color = getInactiveBorderColor();
        }
        else
        {
            color = getBorderColor();
        }
        
        if (color != null)
        {
            g.setColor(color);
            g.drawRect(0, 0, width() - 1, height() - 1);
        }
    }
    
    protected void paintLock(final Graphics g)
    {
        if (true || !isActive() || !isLocked())
        {
            return;
        }
        
        final Color color = Color.RED;
        g.setColor(color);
        g.drawLine(0, 0, width(), height());
        g.drawLine(width(), 0, 0, height());
    }
    
    protected void leftButtonPressed() { }
    
    protected void leftButtonReleased() { }
    
    protected void leftButtonEntered() { }
    
    protected void leftButtonExited() { }
    
    protected void rightButtonPressed() { }
    
    protected void rightButtonReleased() { }
    
    protected void rightButtonEntered() { }
    
    protected void rightButtonExited() { }
    
    protected void noButtonEntered() { }
    
    protected void noButtonExited() { }
    
    @Override
    public void mouseClicked(final MouseEvent e) { }
    
    @Override
    public void mouseEntered(final MouseEvent e)
    {
        isHighlighted = true;
        
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
        {
            leftButtonEntered();
        }
        else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
        {
            rightButtonEntered();
        }
        else
        {
            noButtonEntered();
        }
        
        repaint();
    }
    
    @Override
    public void mouseExited(final MouseEvent e)
    {
        isHighlighted = false;
        
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
        {
            leftButtonExited();
        }
        else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
        {
            rightButtonExited();
        }
        else
        {
            noButtonExited();
        }
        
        repaint();
    }
    
    @Override
    public void mousePressed(final MouseEvent e)
    {
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
        {
            leftButtonPressed();
        }
        else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
        {
            rightButtonPressed();
        }
        
        repaint();
    }
    
    @Override
    public void mouseReleased(final MouseEvent e)
    {
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
        {
            leftButtonReleased();
        }
        else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
        {
            rightButtonReleased();
        }
        
        repaint();
    }
}
