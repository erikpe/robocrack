package robocrack.gui;

import java.util.Observable;
import java.util.Observer;

public class GuiModel extends Observable
{
    public static enum BoardButton
    {
        RED_BUTTON,
        BLUE_BUTTON,
        GREEN_BUTTON,
        STAR_BUTTON,
        ARROW_BUTTON,
    }
    
    public static enum FunctionButton
    {
        FORWARD_BUTTON,
        LEFT_BUTTON,
        RIGHT_BUTTON,
        
        F1_BUTTON,
        F2_BUTTON,
        F3_BUTTON,
        F4_BUTTON,
        F5_BUTTON,
        
        RED_BUTTON,
        GREEN_BUTTON,
        BLUE_BUTTON,
        NO_COLOR_BUTTON,
        CLEAR_BUTTON
    }
    
    private boolean star;
    private BoardButton selectedBoardButton;
    private FunctionButton selectedFunctionButton;
    
    GuiModel()
    {
        this.star = true;
        this.selectedBoardButton = BoardButton.values()[0];
        this.selectedFunctionButton = FunctionButton.values()[0];
    }
    
    boolean isSelected(final Enum<?> buttonEnum)
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
    
    void selectButton(final Enum<?> buttonEnum)
    {
        if (buttonEnum instanceof BoardButton)
        {
            this.selectedBoardButton = (BoardButton) buttonEnum;
        }
        else if (buttonEnum instanceof FunctionButton)
        {
            this.selectedFunctionButton = (FunctionButton) buttonEnum;
        }
        
        setChanged();
        notifyObservers();
    }
    
    BoardButton selectedBoardButton()
    {
        return selectedBoardButton;
    }
    
    FunctionButton selectedFunctionButton()
    {
        return selectedFunctionButton;
    }
    
    void setStar(final boolean star)
    {
        this.star = star;
    }
    
    boolean getStar()
    {
        return star;
    }
    
    @Override
    public void addObserver(final Observer observer)
    {
        super.addObserver(observer);
        
        setChanged();
        notifyObservers();
    }
}
