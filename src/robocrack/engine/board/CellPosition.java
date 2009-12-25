package robocrack.engine.board;

import robocrack.util.WeakCache;

public final class CellPosition
{
    private final static WeakCache<CellPosition> cache =
        new WeakCache<CellPosition>();
    
    public final int x;
    public final int y;
    
    private CellPosition(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public static CellPosition make(final int x, final int y)
    {
        if (x < 0 || y < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        
        return cache.get(new CellPosition(x, y));
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
        
        if (!(other instanceof CellPosition))
        {
            return false;
        }
        
        final CellPosition otherPosition = (CellPosition) other;
        return x == otherPosition.x && y == otherPosition.y;
    }
}
