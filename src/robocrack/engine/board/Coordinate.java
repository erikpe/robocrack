package robocrack.engine.board;

import robocrack.util.WeakCache;

public final class Coordinate
{
    private final static WeakCache<Coordinate> cache =
        new WeakCache<Coordinate>();
    
    public final int x;
    public final int y;
    
    private Coordinate(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public static Coordinate make(final int x, final int y)
    {
        if (x < 0 || y < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        
        return cache.get(new Coordinate(x, y));
    }
    
    @Override
    public String toString()
    {
        return "<" + x + ", " + y + ">";
    }
    
    @Override
    public boolean equals(final Object other)
    {
        if (this == other)
        {
            return true;
        }
        
        if (other == null)
        {
            return false;
        }
        
        if (!(other instanceof Coordinate))
        {
            return false;
        }
        
        final Coordinate otherCoordinate = (Coordinate) other;
        return x == otherCoordinate.x && y == otherCoordinate.y;
    }
}
