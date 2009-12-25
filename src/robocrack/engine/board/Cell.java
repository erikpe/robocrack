package robocrack.engine.board;

import robocrack.engine.board.BoardModel.CellColor;

public class Cell
{
    final int x;
    final int y;
    
    Cell leftNeighbour = null;
    Cell rightNeighbour = null;
    Cell upNeighbour = null;
    Cell downNeighbour = null;
    
    CellColor color;
    boolean hasStar;
    final CellPosition cellPosition;
    
    Cell(final CellPosition cellPosition)
    {
        this.x = cellPosition.x;
        this.y = cellPosition.y;
        this.color = CellColor.NONE;
        this.hasStar = false;
        this.cellPosition = cellPosition;
    }
    
    public CellPosition getCoordinate()
    {
        return cellPosition;
    }
    
    public void setColor(final CellColor color)
    {
        this.color = color;
    }
    
    public CellColor getColor()
    { 
        return color;
    }
    
    public void setStar(final boolean hasStar)
    {
        this.hasStar = hasStar;
    }
    
    @Override
    public String toString()
    {
        return "[" + cellPosition + ", " + color + (hasStar ? ", [X]" : "") + "]";
    }
}
