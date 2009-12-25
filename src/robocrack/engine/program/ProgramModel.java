package robocrack.engine.program;

import java.util.Observable;

public class ProgramModel extends Observable
{
    public static enum OpCode
    {
        NOP,
        GO_FORWARD,
        TURN_LEFT,
        TURN_RIGHT,
        
        CALL_F1,
        CALL_F2,
        CALL_F3,
        CALL_F4,
        CALL_F5
    }
    
    public static enum Condition
    {
        ON_ALL,
        ON_RED,
        ON_GREEN,
        ON_BLUE
    }
    
    private static final int MAX_FUNCTIONS = 5;
    private static final int MAX_FUNCTION_LENGTH = 10;
    
    private static final OpCode DEFAULT_OPCODE = OpCode.NOP;
    private static final Condition DEFAULT_CONDITION = Condition.ON_ALL;
    
    private final int[] functionLength;
    private final Instruction[][] program;
    
    public ProgramModel()
    {
        this.functionLength = new int[MAX_FUNCTIONS];
        this.program = new Instruction[MAX_FUNCTIONS][];
        
        initialize();
    }
    
    private void initialize()
    {
        for (int i = 0; i < MAX_FUNCTIONS; ++i)
        {
            functionLength[i] = 0;
            program[i] = new Instruction[MAX_FUNCTION_LENGTH];

            for (int j = 0; j < MAX_FUNCTION_LENGTH; ++j)
            {
                program[i][j] = new Instruction(DEFAULT_OPCODE,
                        DEFAULT_CONDITION);
            }
        }
        
        setFunctionLength(1, 3);
        setFunctionLength(2, 3);
    }
    
    public int getMaxFunctions()
    {
        return MAX_FUNCTIONS;
    }
    
    public int getMaxFunctionLength()
    {
        return MAX_FUNCTION_LENGTH;
    }
    
    public int getFunctionLength(final int function)
    {
        return functionLength[function - 1];
    }
    
    public void setFunctionLength(final int function, int length)
    {
        if (function > getMaxFunctions() || function <= 0)
        {
            return;
        }
        
        length = Math.max(0, length);
        length = Math.min(getMaxFunctionLength(), length);
        
        if (function == 1)
        {
            length = Math.max(1, length);
        }
        else if (getFunctionLength(function - 1) == 0)
        {
            length = 0;
        }
        else if (function < getMaxFunctions() && getFunctionLength(function + 1) > 0)
        {
            length = Math.max(1, length);
        }
        
        functionLength[function - 1] = length;
        
        setChanged();
        notifyObservers();
    }
    
    public boolean isActive(final InstructionPosition position)
    {
        return position.slot < getFunctionLength(position.function);
    }
}
