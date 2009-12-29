package robocrack.engine.board;

import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.board.BoardModel.Mode;

public interface BoardSimulator extends BoardViewer
{
    public void simGoForward();

    public void simTurnLeft();

    public void simTurnRight();

    public Mode getMode();

    public void startSimulation();
    
    public void resetSimulation();
    
    public Cell getCurrentCell();
    
    public void simPaintColor(final CellColor color);
}
