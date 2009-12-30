package robocrack.gui;

import java.util.Observable;

import robocrack.engine.program.InstructionPosition;
import robocrack.engine.simulator.Simulator.StackDepth;

public class GuiModel extends Observable
{
    public static enum BoardButton
    {
        RED_BUTTON("R"),
        GREEN_BUTTON("G"),
        BLUE_BUTTON("B"),
        STAR_BUTTON("S"),
        ARROW_BUTTON("A");
        
        private final String string;
        
        private BoardButton(final String string)
        {
            this.string = string;
        }
        
        @Override
        public String toString()
        {
            return string;
        }
    }
    
    public static enum FunctionButton
    {
        FORWARD_BUTTON("F"),
        LEFT_BUTTON("L"),
        RIGHT_BUTTON("R"),
        
        F1_BUTTON("F1"),
        F2_BUTTON("F2"),
        F3_BUTTON("F3"),
        F4_BUTTON("F4"),
        F5_BUTTON("F5"),
        
        PAINT_RED_BUTTON("PR"),
        PAINT_GREEN_BUTTON("PG"),
        PAINT_BLUE_BUTTON("PB"),
        
        RED_BUTTON("R"),
        GREEN_BUTTON("G"),
        BLUE_BUTTON("B"),
        NO_COLOR_BUTTON("NC"),
        CLEAR_BUTTON("C");
        
        private final String string;
        
        private FunctionButton(final String string)
        {
            this.string = string;
        }
        
        @Override
        public String toString()
        {
            return string;
        }
    }
    
    private boolean star;
    private BoardButton selectedBoardButton;
    private FunctionButton selectedFunctionButton;
    private StackDepth stackDepthHighlight;
    private InstructionPosition instPosHighlight;
    
    GuiModel()
    {
        this.star = true;
        this.selectedBoardButton = BoardButton.values()[0];
        this.selectedFunctionButton = FunctionButton.values()[0];
        
        this.stackDepthHighlight = null;
        this.instPosHighlight = null;
    }
    
    public boolean isSelected(final Enum<?> buttonEnum)
    {
        if (buttonEnum instanceof BoardButton)
        {
            return buttonEnum == selectedBoardButton();
        }
        else if (buttonEnum instanceof FunctionButton)
        {
            return buttonEnum == selectedFunctionButton();
        }
        
        assert false;
        return false;
    }
    
    public void selectButton(final Enum<?> buttonEnum)
    {
        final Enum<?> oldButton;
        
        if (buttonEnum instanceof BoardButton)
        {
            oldButton = selectedBoardButton;
            selectedBoardButton = (BoardButton) buttonEnum;
        }
        else
        {
            assert buttonEnum instanceof FunctionButton;
            oldButton = selectedFunctionButton;
            selectedFunctionButton = (FunctionButton) buttonEnum;
        }
        
        setChanged();
        notifyObservers(oldButton);
        
        setChanged();
        notifyObservers(buttonEnum);
    }
    
    public BoardButton selectedBoardButton()
    {
        return selectedBoardButton;
    }
    
    public FunctionButton selectedFunctionButton()
    {
        return selectedFunctionButton;
    }
    
    public void setStar(final boolean star)
    {
        this.star = star;
    }
    
    public boolean getStar()
    {
        return star;
    }
    
    public void setStackDepthHighlight(final StackDepth depth)
    {
        final StackDepth oldHighlight = stackDepthHighlight;
        stackDepthHighlight = depth;
        
        if (oldHighlight != null)
        {
            setChanged();
            notifyObservers(oldHighlight);
        }
        
        if (stackDepthHighlight != null)
        {
            setChanged();
            notifyObservers(stackDepthHighlight);
        }
    }
    
    public StackDepth getStackDepthHighlight()
    {
        return stackDepthHighlight;
    }
    
    public void setInstPosHighlight(final InstructionPosition position)
    {
        final InstructionPosition oldHighlight = instPosHighlight;
        instPosHighlight = position;
        
        if (oldHighlight != null)
        {
            setChanged();
            notifyObservers(oldHighlight);
        }
        
        if (instPosHighlight != null)
        {
            setChanged();
            notifyObservers(instPosHighlight);
        }
    }
    
    public InstructionPosition getInstPosHighlight()
    {
        return instPosHighlight;
    }
}
