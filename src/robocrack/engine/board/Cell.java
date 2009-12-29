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
    
    final CellPosition cellPosition;
    
    CellColor color;
    boolean hasStar;
    
    CellColor simColor;
    boolean simHasStar;
    
    Cell(final CellPosition cellPosition)
    {
        this.x = cellPosition.x;
        this.y = cellPosition.y;
        
        this.color = CellColor.NONE;
        this.simColor = CellColor.NONE;
        
        this.hasStar = false;
        this.simHasStar = false;
        
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
    
    boolean resetSimulation()
    {
        return simColor != color || simHasStar != hasStar;
    }
    
    void startSimulation()
    {
        simColor = color;
        simHasStar = hasStar;
    }
    
    @Override
    public String toString()
    {
        return "[" + cellPosition + ", " + color + (hasStar ? ", [X]" : "") + "]";
    }
}
