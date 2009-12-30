package robocrack.engine.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import robocrack.engine.board.BoardSimulator;
import robocrack.engine.board.Cell;
import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;

public class Simulator extends Observable
{
    public static enum SimulatorState
    {
        RESET,
        RUNNING,
        HALTED;
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
    
    private final BoardSimulator boardSimulator;
    private final ProgramModel programModel;
    
    private InstructionPosition programCounter;
    private List<InstructionPosition> stack;
    private SimulatorState state;
    
    public Simulator(final BoardSimulator boardSimulator,
            final ProgramModel programModel)
    {
        this.boardSimulator = boardSimulator;
        this.programModel = programModel;
        
        this.programCounter = InstructionPosition.make(1, 0);
        this.stack = new ArrayList<InstructionPosition>();
        this.state = SimulatorState.RESET;
    }
    
    public void step()
    {
        if (getState() == SimulatorState.HALTED)
        {
            return;
        }
        else if (getState() == SimulatorState.RESET)
        {
            boardSimulator.startSimulation();
            setState(SimulatorState.RUNNING);
        }
        
        final Instruction currentInstruction = programModel
                .instructionAt(programCounter);
        
        execute(currentInstruction);
    }
    
    public void reset()
    {
        if (getState() != SimulatorState.RESET)
        {
            setState(SimulatorState.RESET);
        }
        
        while (!stack.isEmpty())
        {
            popStack();
        }
        
        jumpTo(InstructionPosition.make(1, 0));
        
        boardSimulator.resetSimulation();
    }
    
    private void execute(final Instruction instruction)
    {
        final Cell cell = boardSimulator.getCurrentCell();
        
        if (skip(cell.getColor(), instruction.condition))
        {
            nextInstruction();
            return;
        }
        
        switch (instruction.opCode)
        {
        case NOP:
            nextInstruction();
            break;
            
        case GO_FORWARD:
            boardSimulator.simGoForward();
            nextInstruction();
            break;
            
        case TURN_LEFT:
            boardSimulator.simTurnLeft();
            nextInstruction();
            break;
            
        case TURN_RIGHT:
            boardSimulator.simTurnRight();
            nextInstruction();
            break;
            
        case PAINT_RED:
            boardSimulator.simPaintColor(CellColor.RED);
            nextInstruction();
            break;
            
        case PAINT_GREEN:
            boardSimulator.simPaintColor(CellColor.GREEN);
            nextInstruction();
            break;
            
        case PAINT_BLUE:
            boardSimulator.simPaintColor(CellColor.BLUE);
            nextInstruction();
            break;
            
        case CALL_F1:
            call(1);
            break;
            
        case CALL_F2:
            call(2);
            break;
            
        case CALL_F3:
            call(3);
            break;
            
        case CALL_F4:
            call(4);
            break;
            
        case CALL_F5:
            call(5);
            break;
        }
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
    
    private void call(final int function)
    {
        pushStack();
        jumpTo(InstructionPosition.make(function, 0));
    }
    
    private InstructionPosition nextPosition(final InstructionPosition position)
    {
        final int func = position.function;
        final int slot = position.slot + 1;
        
        if (slot >= programModel.getFunctionLength(func))
        {
            return null;
        }
        
        return InstructionPosition.make(func, slot);
    }
    
    private void nextInstruction()
    {
        InstructionPosition newPosition = nextPosition(programCounter);
        
        while (newPosition == null && !stack.isEmpty())
        {
            newPosition = nextPosition(popStack());
        }
        
        if (newPosition == null)
        {
            setState(SimulatorState.HALTED);
        }
        else
        {
            jumpTo(newPosition);
        }
    }
    
    private void jumpTo(final InstructionPosition newPosition)
    {
        final InstructionPosition oldPosition = programCounter;
        programCounter = newPosition;
        
        setChanged();
        notifyObservers(oldPosition);
        setChanged();
        notifyObservers(newPosition);
    }
    
    private void pushStack()
    {
        stack.add(programCounter);
        
        setChanged();
        notifyObservers(new StackDepth(stack.size() - 1));
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
        
        programModel.setState(getState());
    }
}
