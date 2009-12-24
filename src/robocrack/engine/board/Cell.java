package robocrack.engine.board;

public class Cell implements Position
{
    public static enum CellColor
    {
        NONE,
        RED,
        GREEN,
        BLUE
    }
    
    private final int x;
    private final int y;
    
    Cell leftNeighbour = null;
    Cell rightNeighbour = null;
    Cell upNeighbour = null;
    Cell downNeighbour = null;
    
    CellColor color;
    boolean hasStar;
    final Coordinate coordinate;
    
    Cell(final Coordinate coordinate)
    {
        this.x = coordinate.x;
        this.y = coordinate.y;
        this.color = CellColor.NONE;
        this.hasStar = false;
        this.coordinate = coordinate;
    }
    
    @Override
    public int x()
    {
        return x;
    }
    
    @Override
    public int y()
    {
        return y;
    }
    
    public Coordinate getCoordinate()
    {
        return coordinate;
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
        return "[" + coordinate + ", " + color + (hasStar ? ", [X]" : "") + "]";
    }
}
