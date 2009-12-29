package robocrack.engine.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import robocrack.engine.board.BoardSimulator;
import robocrack.engine.board.Cell;
import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.board.BoardModel.Mode;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;

public class Simulator extends Observable
{
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
    
    public Simulator(final BoardSimulator boardSimulator, final ProgramModel programModel)
    {
        this.boardSimulator = boardSimulator;
        this.programModel = programModel;
        
        this.programCounter = InstructionPosition.make(1, 0);
        this.stack = new ArrayList<InstructionPosition>();
    }
    
    public void step()
    {
        if (boardSimulator.getMode() != Mode.SIMULATE)
        {
            boardSimulator.startSimulation();
        }
        
        final Instruction currentInstruction = programModel
                .instructionAt(programCounter);
        
        execute(currentInstruction);
    }
    
    public void reset()
    {
        boardSimulator.resetSimulation();
        
        while (!stack.isEmpty())
        {
            popStack();
        }
        
        jumpTo(InstructionPosition.make(1, 0));
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
    
    private void nextInstruction()
    {
        InstructionPosition newPosition = InstructionPosition.make(
                programCounter.function, programCounter.slot + 1);
        
        while (newPosition.slot >= programModel
                .getFunctionLength(newPosition.function)
                && !stack.isEmpty())
        {
            final InstructionPosition poppedPosition = popStack();
            newPosition = InstructionPosition.make(poppedPosition.function,
                    poppedPosition.slot + 1);
        }
        
        jumpTo(newPosition);
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
}
