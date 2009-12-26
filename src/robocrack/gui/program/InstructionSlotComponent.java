package robocrack.gui.program;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;
import robocrack.gui.GuiModel;
import robocrack.gui.GuiModel.FunctionButton;
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
    private final JLabel label;
    
    InstructionSlotComponent(final ProgramModel programModel, GuiModel guiModel,
            final InstructionPosition position)
    {
        this.programModel = programModel;
        this.guiModel = guiModel;
        this.position = position;
        this.label = new JLabel();
        
        initialize();
    }
    
    private void initialize()
    {
        label.setForeground(Color.WHITE);
        label.setVisible(false);
        add(label);
        
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
        paintLabel();
    }
    
    private void paintLabel()
    {
        final OpCode opCode = programModel.getOpCode(position);
        final String text = textFromOpCode(opCode);
        
        if (text == null)
        {
            label.setVisible(false);
            return;
        }
        
        label.setText(text);
        
        final int labelWidth = label.getPreferredSize().width;
        final int labelHeight = label.getPreferredSize().height;
        
        final int xBounds = (width() - labelWidth) / 2;
        final int yBounds = (height() - labelHeight) / 2;
        
        label.setBounds(xBounds, yBounds, labelWidth, labelHeight);
        label.setVisible(true);
    }
    
    private String textFromOpCode(final OpCode opCode)
    {
        switch (opCode)
        {
        case CALL_F1: return "F1";
        case CALL_F2: return "F2";
        case CALL_F3: return "F3";
        case CALL_F4: return "F4";
        case CALL_F5: return "F5";
        default: return null;
        }
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
        case ON_ALL: return Color.GRAY;
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
    
    private OpCode opCodeFromButton(final FunctionButton button)
    {
        switch (button)
        {
        case FORWARD_BUTTON: return OpCode.GO_FORWARD;
        case LEFT_BUTTON: return OpCode.TURN_LEFT;
        case RIGHT_BUTTON: return OpCode.TURN_RIGHT;
        case F1_BUTTON: return OpCode.CALL_F1;
        case F2_BUTTON: return OpCode.CALL_F2;
        case F3_BUTTON: return OpCode.CALL_F3;
        case F4_BUTTON: return OpCode.CALL_F4;
        case F5_BUTTON: return OpCode.CALL_F5;
        case PAINT_RED_BUTTON: return OpCode.PAINT_RED;
        case PAINT_GREEN_BUTTON: return OpCode.PAINT_GREEN;
        case PAINT_BLUE_BUTTON: return OpCode.PAINT_BLUE;
        case CLEAR_BUTTON: return OpCode.NOP;
        default: return null;
        }
    }
    
    private Condition conditionFromButton(final FunctionButton button)
    {
        switch (button)
        {
        case RED_BUTTON: return Condition.ON_RED;
        case GREEN_BUTTON: return Condition.ON_GREEN;
        case BLUE_BUTTON: return Condition.ON_BLUE;
        case NO_COLOR_BUTTON: return Condition.ON_ALL;
        case CLEAR_BUTTON: return Condition.ON_ALL;
        default: return null;
        }
    }
    
    @Override
    protected void leftButtonPressed()
    {
        if (!isActive())
        {
            return;
        }
        
        final FunctionButton button = guiModel.selectedFunctionButton();
        final OpCode opCode = opCodeFromButton(button);
        final Condition condition = conditionFromButton(button);
        
        if (opCode != null)
        {
            programModel.setOpCode(position, opCode);
        }
        
        if (condition != null)
        {
            programModel.setCondition(position, condition);
        }
    }
    
    @Override
    protected void rightButtonPressed()
    {
        if (isActive())
        {
            programModel.clear(position);
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
    }
}
