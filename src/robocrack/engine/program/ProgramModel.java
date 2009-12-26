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
        CALL_F5,
        
        PAINT_RED,
        PAINT_GREEN,
        PAINT_BLUE
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
    private InstructionPosition programCounter;
    
    public ProgramModel()
    {
        this.functionLength = new int[MAX_FUNCTIONS];
        this.program = new Instruction[MAX_FUNCTIONS][];
        this.programCounter = InstructionPosition.make(1, 0);
        
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
    
    private final Instruction instructionAt(final InstructionPosition position)
    {
        return program[position.function - 1][position.slot];
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
    
    public void setFunctionLength(final int function, int newLength)
    {
        newLength = Math.max(0, newLength);
        newLength = Math.min(getMaxFunctionLength(), newLength);
        
        if (function == 1)
        {
            newLength = Math.max(1, newLength);
        }
        else if (getFunctionLength(function - 1) == 0)
        {
            newLength = 0;
        }
        else if (function < getMaxFunctions() && getFunctionLength(function + 1) > 0)
        {
            newLength = Math.max(1, newLength);
        }
        
        final int oldLength = getFunctionLength(function);
        functionLength[function - 1] = newLength;
        
        final int updateFrom = Math.min(oldLength, newLength);
        final int updateTo = Math.max(oldLength, newLength);
        
        for (int slot = updateFrom; slot < updateTo; ++slot)
        {
            clear(InstructionPosition.make(function, slot));
        }
    }
    
    public boolean isActive(final InstructionPosition position)
    {
        return position.slot < getFunctionLength(position.function);
    }
    
    public OpCode getOpCode(final InstructionPosition position)
    {
        return instructionAt(position).opCode;
    }
    
    public Condition getCondition(final InstructionPosition position)
    {
        return instructionAt(position).condition;
    }
    
    public void setOpCode(final InstructionPosition position,
            final OpCode opCode)
    {
        instructionAt(position).opCode = opCode;
        
        setChanged();
        notifyObservers(position);
    }
    
    public void setCondition(final InstructionPosition position,
            final Condition condition)
    {
        instructionAt(position).condition = condition;
        
        setChanged();
        notifyObservers(position);
    }
    
    public void clear(final InstructionPosition position)
    {
        setOpCode(position, OpCode.NOP);
        setCondition(position, Condition.ON_ALL);
    }
    
    public InstructionPosition getProgramCounter()
    {
        return programCounter;
    }
}
