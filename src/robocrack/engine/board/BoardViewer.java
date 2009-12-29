package robocrack.engine.board;

import java.util.Observer;

import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.BoardModel.CellColor;

public interface BoardViewer
{
    public int width();
    
    public int height();
    
    public boolean hasStar(final CellPosition cellPosition);
    
    public CellColor getColor(final CellPosition cellPosition);
    
    public CellPosition getArrowPosition();
    
    public ArrowDirection getArrowDirection();
    
    public void addObserver(final Observer observer);
}
