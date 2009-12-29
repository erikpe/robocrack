package robocrack.gui.simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.StackDepth;
import robocrack.gui.GuiModel;
import robocrack.gui.common.SquareComponent;

@SuppressWarnings("serial")
public class StackComponent extends SquareComponent implements Observer
{
    public static final int WIDTH = 31;
    public static final int HEIGHT = WIDTH;
    
    private final Simulator simulator;
    private final GuiModel guiModel;
    private final StackDepth depth;
    private final JLabel label;
    
    public StackComponent(final Simulator simulator, final GuiModel guiModel,
            final StackDepth depth)
    {
        this.simulator = simulator;
        this.guiModel = guiModel;
        this.depth = depth;
        this.label = new JLabel();
        
        initialize();
    }
    
    private void initialize()
    {
        updateLabel();
        add(label);
        
        simulator.addObserver(this);
        guiModel.addObserver(this);
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
    
    @Override
    protected boolean isActive()
    {
        return simulator.getStackPointerAt(depth) != null;
    }
    
    private void updateLabel()
    {
        final InstructionPosition position = simulator.getStackPointerAt(depth);
        
        if (position == null)
        {
            label.setVisible(false);
            return;
        }
        
        label.setText(position.toString());
        
        final int labelWidth = label.getPreferredSize().width;
        final int labelHeight = label.getPreferredSize().height;
        
        final int xBounds = (width() - labelWidth) / 2;
        final int yBounds = (height() - labelHeight) / 2;
        
        label.setBounds(xBounds, yBounds, labelWidth, labelHeight);
        label.setVisible(true);
    }
    
    @Override
    public void paintComponent(final Graphics g)
    {
        paintBackground(g);
        paintBorder(g);
        paintStackHighlight(g);
    }
    
    private void paintStackHighlight(final Graphics g)
    {
        if (!isActive() || !stackHighlighted())
        {
            return;
        }
        
        final int xBounds = width() - 3 * width() / 8;
        final int yBounds = height() - 3 * height() / 8;
        final int width = width() / 4;
        final int height = height() / 4;
        
        g.setColor(Color.YELLOW);
        g.fillOval(xBounds, yBounds, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(xBounds, yBounds, width, height);
    }

    private boolean stackHighlighted()
    {
        if (depth.equals(guiModel.getStackDepthHighlight()))
        {
            return true;
        }
        
        final InstructionPosition higlightPos = guiModel.getInstPosHighlight();
        final InstructionPosition thisPos = simulator.getStackPointerAt(depth);
        
        return (higlightPos != null && higlightPos.equals(thisPos));
    }
    
    @Override
    public void noButtonEntered()
    {
        guiModel.setStackDepthHighlight(depth);
    }
    
    @Override
    public void noButtonExited()
    {
        guiModel.setStackDepthHighlight(null);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (depth.equals(arg))
        {
            updateLabel();
            repaint();
        }
        else if (arg != null && arg.equals(simulator.getStackPointerAt(depth)))
        {
            updateLabel();
            repaint();
        }
    }
}
