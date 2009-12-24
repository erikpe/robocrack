package robotcrack.gui;

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
    private boolean isHighlighted = false;
    
    SquareComponent()
    {
        addMouseListener(this);
    }
    
    protected abstract Color getBackgroundColor();
    
    protected abstract int width();
    
    protected abstract int height();
    
    @Override
    public void paintComponent(final Graphics g)
    {
        paintBackground(g);
        paintBorder(g);
    }
    
    protected void paintBackground(final Graphics g)
    {
        Color color = getBackgroundColor();
        
        if (isHighlighted)
        {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            
            red = red + 2 * (255 - red) / 3;
            green = green + 2 * (255 - green) / 3;
            blue = blue + 2 * (255 - blue) / 3;
            
            color = new Color(red, green, blue);
        }
        
        g.setColor(color);
        g.fillRect(0, 0, width() - 1, height() - 1);
    }
    
    protected void paintBorder(final Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width() - 1, height() - 1);
    }
    
    void leftButtonPressed() { }
    
    void leftButtonReleased() { }
    
    void leftButtonEntered() { }
    
    void leftButtonExited() { }
    
    void rightButtonPressed() { }
    
    void rightButtonReleased() { }
    
    void rightButtonEntered() { }
    
    void rightButtonExited() { }
    
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
