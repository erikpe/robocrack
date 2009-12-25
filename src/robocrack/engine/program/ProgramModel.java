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
    
    private int numFunctions;
    private final int[] functionLength;
    private final Instruction[][] program;
    
    public ProgramModel()
    {
        this.numFunctions = 1;
        this.functionLength = new int[MAX_FUNCTIONS];
        this.program = new Instruction[MAX_FUNCTIONS][];
        
        initialize();
    }
    
    private void initialize()
    {
        for (int i = 0; i < MAX_FUNCTIONS; ++i)
        {
            functionLength[i] = 1;
            program[i] = new Instruction[MAX_FUNCTION_LENGTH];

            for (int j = 0; j < MAX_FUNCTION_LENGTH; ++j)
            {
                program[i][j] = new Instruction(DEFAULT_OPCODE,
                        DEFAULT_CONDITION);
            }
        }
    }
    
    public int getNumFunctions()
    {
        return numFunctions;
    }
    
    public int getFunctionLength(final int index)
    {
        return functionLength[index];
    }
    
    public boolean isActive(final int function, final int slot)
    {
        return function <= numFunctions && slot < functionLength[function];
    }
}
