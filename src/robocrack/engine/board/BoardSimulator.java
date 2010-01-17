package robocrack.engine.board;

import robocrack.engine.board.BoardModel.CellColor;

public interface BoardSimulator extends BoardViewer
{
    public void simGoForward();

    public void simTurnLeft();

    public void simTurnRight();

    public void startSimulation();
    
    public void resetSimulation();
    
    public Cell getCurrentCell();
    
    public void simPaintColor(final CellColor color);
    
    public int simNumStarsLeft();
}
