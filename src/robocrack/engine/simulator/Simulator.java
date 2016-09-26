package robocrack.engine.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.board.BoardSimulator;
import robocrack.engine.board.Cell;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class Simulator extends Observable
{
    public static enum SimulatorState
    {
        RESET,
        RUNNING,
        HALTED,
        BRUTE_FORCING;
    }

    public static class StackDepth
    {
        public final int intValue;

        public StackDepth(final int depth)
        {
            this.intValue = depth;
        }

        @Override
        public boolean equals(final Object obj)
        {
            return obj != null && obj instanceof StackDepth
                    && ((StackDepth) obj).intValue == intValue;
        }

        @Override
        public String toString()
        {
            return String.valueOf(intValue);
        }
    }

    private final SimulatorRunner simulatorRunner;
    private final BoardSimulator boardSimulator;
    private final ProgramModel programModel;

    private InstructionPosition programCounter;
    private final List<InstructionPosition> stack;
    private SimulatorState state;
    private boolean useTailCallOptimization;

    public Simulator(final BoardSimulator boardSimulator,
            final ProgramModel programModel)
    {
        this.simulatorRunner = new SimulatorRunner(this);
        this.boardSimulator = boardSimulator;
        this.programModel = programModel;

        this.programCounter = null;
        this.stack = new ArrayList<InstructionPosition>();
        this.state = SimulatorState.RESET;
        this.useTailCallOptimization = true;

        programModel.setSimulator(this);
    }

    void step()
    {
        if (getState() == SimulatorState.HALTED)
        {
            return;
        }
        else if (getState() == SimulatorState.RESET)
        {
            setState(SimulatorState.RUNNING);

            boardSimulator.startSimulation();
            nextInstruction(InstructionPosition.make(1, -1));

            if (boardSimulator.getCurrentCell().simGetColor() == CellColor.NONE)
            {
                setState(SimulatorState.HALTED);
            }
        }
        else
        {
            final Instruction currentInstruction = programModel
                    .instructionAt(programCounter);

            execute(currentInstruction);
        }
    }

    void reset()
    {
        if (getState() != SimulatorState.RESET)
        {
            setState(SimulatorState.RESET);
        }

        while (!stack.isEmpty())
        {
            popStack();
        }

        jumpTo(null);
        boardSimulator.resetSimulation();
    }

    private void execute(final Instruction instruction)
    {
        final Cell cell = boardSimulator.getCurrentCell();

        if (skip(cell.simGetColor(), instruction.condition))
        {
            nextInstruction();
            return;
        }

        switch (instruction.opCode)
        {
        case NOP: nextInstruction(); break;
        case GO_FORWARD: goForward(); break;
        case TURN_LEFT: turnLeft(); break;
        case TURN_RIGHT: turnRight(); break;
        case PAINT_RED: paint(CellColor.RED); break;
        case PAINT_GREEN: paint(CellColor.GREEN); break;
        case PAINT_BLUE: paint(CellColor.BLUE); break;
        case CALL_F1: call(1); break;
        case CALL_F2: call(2); break;
        case CALL_F3: call(3); break;
        case CALL_F4: call(4); break;
        case CALL_F5: call(5); break;
        }
    }

    private void goForward()
    {
        boardSimulator.simGoForward();

        if (boardSimulator.simNumStarsLeft() == 0)
        {
            setState(SimulatorState.HALTED);
        }
        if (boardSimulator.getCurrentCell().simGetColor() == CellColor.NONE)
        {
            setState(SimulatorState.HALTED);
        }
        else
        {
            nextInstruction();
        }
    }

    private void turnLeft()
    {
        boardSimulator.simTurnLeft();
        nextInstruction();
    }

    private void turnRight()
    {
        boardSimulator.simTurnRight();
        nextInstruction();
    }

    private void paint(final CellColor color)
    {
        boardSimulator.simPaintColor(color);
        nextInstruction();
    }

    private void call(final int function)
    {
        pushStack();
        nextInstruction(InstructionPosition.make(function, -1));
    }

    private boolean skip(final CellColor cellColor, final Condition condition)
    {
        switch (condition)
        {
        case ON_ALL: return false;
        case ON_RED: return cellColor != CellColor.RED;
        case ON_GREEN: return cellColor != CellColor.GREEN;
        case ON_BLUE: return cellColor != CellColor.BLUE;
        default: return true;
        }
    }

    private InstructionPosition nextPosition(final InstructionPosition position)
    {
        final int func = position.function;
        final int slot = position.slot + 1;

        if (slot >= programModel.getFunctionLength(func))
        {
            return null;
        }

        final InstructionPosition newPos = InstructionPosition.make(func, slot);

        if (programModel.getOpCode(newPos) == OpCode.NOP)
        {
            return nextPosition(newPos);
        }

        return newPos;
    }

    private void nextInstruction()
    {
        nextInstruction(getProgramCounter());
    }

    private void nextInstruction(final InstructionPosition position)
    {
        InstructionPosition newPosition = nextPosition(position);

        while (newPosition == null && !stack.isEmpty())
        {
            newPosition = nextPosition(popStack());
        }

        if (newPosition == null)
        {
            setState(SimulatorState.HALTED);
        }

        jumpTo(newPosition);
    }

    private void jumpTo(final InstructionPosition newPosition)
    {
        final InstructionPosition oldPosition = programCounter;
        programCounter = newPosition;

        if (oldPosition != null)
        {
            setChanged();
            notifyObservers(oldPosition);
        }

        if (newPosition != null)
        {
            setChanged();
            notifyObservers(newPosition);
        }
    }

    private void pushStack()
    {
        if (nextPosition(getProgramCounter()) != null || !useTailCallOptimization)
        {
            stack.add(getProgramCounter());

            setChanged();
            notifyObservers(new StackDepth(stack.size() - 1));
        }
    }

    private InstructionPosition popStack()
    {
        final InstructionPosition position = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);

        setChanged();
        notifyObservers(new StackDepth(stack.size()));

        return position;
    }

    public InstructionPosition getProgramCounter()
    {
        return programCounter;
    }

    public InstructionPosition getStackPointerAt(final StackDepth depth)
    {
        if (depth != null && depth.intValue < stack.size())
        {
            return stack.get(depth.intValue);
        }

        return null;
    }

    public SimulatorState getState()
    {
        return state;
    }

    private void setState(final SimulatorState newState)
    {
        state = newState;

        setChanged();
        notifyObservers(getState());
    }

    public SimulatorRunner getRunner()
    {
        return simulatorRunner;
    }

    public boolean getTailCallOptimization()
    {
        return useTailCallOptimization;
    }

    public void setTailCallOptimization(final boolean optimize)
    {
        useTailCallOptimization = optimize;

        setChanged();
        notifyObservers();
    }

    public void isBruteForcing(final boolean isBruteForcing)
    {
        if (isBruteForcing)
        {
            setState(SimulatorState.BRUTE_FORCING);
            boardSimulator.startSimulation();
        }
        else
        {
            setState(SimulatorState.RESET);
            boardSimulator.resetSimulation();
        }
    }
}
