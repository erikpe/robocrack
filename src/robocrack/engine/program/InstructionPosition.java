package robocrack.engine.program;

import robocrack.util.WeakCache;

public class InstructionPosition
{
    private final static WeakCache<InstructionPosition> cache =
        new WeakCache<InstructionPosition>();
    
    public final int function;
    public final int slot;
    
    private InstructionPosition(final int function, final int slot)
    {
        this.function = function;
        this.slot = slot;
    }
    
    public static InstructionPosition make(final int function, final int slot)
    {
        if (function < 0 || slot < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        
        return cache.get(new InstructionPosition(function, slot));
    }
    
    @Override
    public String toString()
    {
        return "f" + function + ":" + slot;
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
        
        if (!(other instanceof InstructionPosition))
        {
            return false;
        }
        
        final InstructionPosition otherPosition = (InstructionPosition) other;
        return function == otherPosition.function && slot == otherPosition.slot;
    }
}
