package robocrack.engine.board;

import robocrack.engine.board.BoardModel.CellColor;

public class Cell
{
    public static final Cell NO_CELL = new Cell(CellPosition.make(-1, -1));
    
    static
    {
        NO_CELL.leftNeighbour = NO_CELL;
        NO_CELL.rightNeighbour = NO_CELL;
        NO_CELL.upNeighbour = NO_CELL;
        NO_CELL.downNeighbour = NO_CELL;
    }
    
    Cell leftNeighbour;
    Cell rightNeighbour;
    Cell upNeighbour;
    Cell downNeighbour;
    
    final CellPosition cellPosition;
    
    CellColor color;
    boolean hasStar;
    
    CellColor simColor;
    boolean simHasStar;
    
    Cell(final CellPosition cellPosition)
    {
        this.color = CellColor.NONE;
        this.simColor = CellColor.NONE;
        
        this.hasStar = false;
        this.simHasStar = false;
        
        this.cellPosition = cellPosition;
        
        this.leftNeighbour = NO_CELL;
        this.rightNeighbour = NO_CELL;
        this.upNeighbour = NO_CELL;
        this.downNeighbour = NO_CELL;
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
