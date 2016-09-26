package robocrack.engine.program;

import java.util.EnumSet;
import java.util.Observable;

import robocrack.engine.simulator.Simulator;
import robocrack.engine.simulator.Simulator.SimulatorState;

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
    private final InstructionPosition programCounter;
    private Simulator simulator;
    private final EnumSet<OpCode> allowedOpCodes;

    public ProgramModel()
    {
        this.functionLength = new int[MAX_FUNCTIONS];
        this.program = new Instruction[MAX_FUNCTIONS][];
        this.programCounter = InstructionPosition.make(1, 0);
        this.allowedOpCodes = EnumSet.allOf(OpCode.class);

        initialize();
    }

    private void initialize()
    {
        allowedOpCodes.remove(OpCode.PAINT_RED);
        allowedOpCodes.remove(OpCode.PAINT_GREEN);
        allowedOpCodes.remove(OpCode.PAINT_BLUE);
        updateCallFunctions();

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

    private void updateCallFunctions()
    {
        setAllowed(OpCode.CALL_F2, getFunctionLength(2) > 0);
        setAllowed(OpCode.CALL_F3, getFunctionLength(3) > 0);
        setAllowed(OpCode.CALL_F4, getFunctionLength(4) > 0);
        setAllowed(OpCode.CALL_F5, getFunctionLength(5) > 0);
    }

    public void setAllowed(final OpCode opCode, final boolean allowed)
    {
        if (isLocked())
        {
            setChanged();
            notifyObservers();

            return;
        }

        if (allowed)
        {
            allow(opCode);
        }
        else
        {
            disallow(opCode);
        }
    }

    private void allow(final OpCode opCode)
    {
        allowedOpCodes.add(opCode);

        setChanged();
        notifyObservers();
    }

    private void disallow(final OpCode opCode)
    {
        allowedOpCodes.remove(opCode);

        for (int function = 1; function < getMaxFunctions(); ++function)
        {
            for (int slot = 0; slot < getFunctionLength(function); ++slot)
            {
                final InstructionPosition pos = InstructionPosition.make(
                        function, slot);

                if (getOpCode(pos) == opCode)
                {
                    setOpCode(pos, OpCode.NOP);
                }
            }
        }

        setChanged();
        notifyObservers();
    }

    public boolean isAllowed(final OpCode opCode)
    {
        return allowedOpCodes.contains(opCode);
    }

    public final Instruction instructionAt(final InstructionPosition position)
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
        if (isLocked())
        {
            return;
        }

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

        if (oldLength != newLength && (oldLength == 0 || newLength == 0))
        {
            updateCallFunctions();
        }

        setChanged();
        notifyObservers(this);
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
        if (isLocked() || !isAllowed(opCode))
        {
            return;
        }

        instructionAt(position).opCode = opCode;

        setChanged();
        notifyObservers(position);
    }

    public void setCondition(final InstructionPosition position,
            final Condition condition)
    {
        if (isLocked())
        {
            return;
        }

        instructionAt(position).condition = condition;

        setChanged();
        notifyObservers(position);
    }

    public void clear(final InstructionPosition position)
    {
        if (isLocked())
        {
            return;
        }

        setOpCode(position, OpCode.NOP);
        setCondition(position, Condition.ON_ALL);
    }

    public InstructionPosition getProgramCounter()
    {
        return programCounter;
    }

    public boolean isLocked()
    {
        if (simulator != null)
        {
            return simulator.getState() != SimulatorState.RESET;
        }

        return false;
    }

    public void setSimulator(final Simulator simulator)
    {
        this.simulator = simulator;
    }
}
