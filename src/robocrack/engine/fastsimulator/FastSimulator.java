package robocrack.engine.fastsimulator;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.fastsimulator.FastBoard.Cell;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;

public class FastSimulator
{
    private final ProgramGenerator programGenerator;
    private final FastBoard fastBoard;
    
    private final Instruction[][] program;
    private final Cell[] board;
    
    private final int[] funcStack = new int[1024];
    private final int[] slotStack = new int[1024];
    
    public FastSimulator(final BoardModel boardModel,
            final ProgramModel programModel)
    {
        this.programGenerator = new ProgramGenerator(programModel);
        this.fastBoard = new FastBoard(boardModel);
        
        this.program = programGenerator.program;
        this.board = fastBoard.board;
    }
    
    public Instruction[][] bruteForce()
    {
        final long startTime = System.currentTimeMillis();
        
        int testedPrograms = 0;
        long simsteps = 0;
        
        do
        {
            simsteps += simulate();
            testedPrograms++;
            
            if (fastBoard.starsLeft == 0)
            {
                final long stopTime = System.currentTimeMillis();
                final double time = (stopTime - startTime) / 1000.0;
                
                System.out.println("Found solution after trying "
                        + testedPrograms + " programs in " + time + " sec ("
                        + (testedPrograms / time) + " progs/s)");
                
                System.out.println("Total number of simulation steps: "
                        + simsteps + " (" + (((double) simsteps) / time)
                        + " steps/s, avg: "
                        + (((double) simsteps) / testedPrograms)
                        + " steps/prog)");
                
                printProgram();
                return program;
            }
            
            fastBoard.reset();
            programGenerator.nextProgram();
        } while (programGenerator.hasNext);
        
        final long stopTime = System.currentTimeMillis();
        final double time = (stopTime - startTime) / 1000.0;
        
        System.out.println("Failed to find a solution after trying "
                + testedPrograms + " programs in " + time + " sec ("
                + (testedPrograms / time) + " progs/s)");
        
        System.out.println("Total number of simulation steps: "
                + simsteps + " (" + (((double) simsteps) / time)
                + " steps/s, avg: "
                + (((double) simsteps) / testedPrograms)
                + " steps/prog)");
        
        return null;
    }
    
    private int simulate()
    {
        int arrowPos = 0;
        ArrowDirection arrowDir = fastBoard.arrowDirection;
        
        int pcFunc = 0;
        int pcSlot = 0;
        
        int simsteps = 0;
        int stackPtr = 0;
        
        Condition condition;
        int callFun;
        
        while (true)
        {
            simsteps++;
            
            if (simsteps > 100)
            {
                return simsteps;
            }
            
            condition = program[pcFunc][pcSlot].condition;
            
            callFun = -1;
            
            if (condition == Condition.ON_ALL || condition == board[arrowPos].color)
            {
                switch (program[pcFunc][pcSlot].opCode)
                {
                case GO_FORWARD:
                    switch (arrowDir)
                    {
                    case UP: arrowPos = board[arrowPos].up; break;
                    case DOWN: arrowPos = board[arrowPos].down; break;
                    case LEFT: arrowPos = board[arrowPos].left; break;
                    case RIGHT: arrowPos = board[arrowPos].right; break;
                    }
                    
                    if (arrowPos < 0)
                    {
                        return simsteps;
                    }
                    
                    if (board[arrowPos].hasStar)
                    {
                        fastBoard.takenStars[--fastBoard.starsLeft] = arrowPos;
                        board[arrowPos].hasStar = false;
                    }
                    
                    if (fastBoard.starsLeft == 0)
                    {
                        return simsteps;
                    }
                    
                    break;
                    
                case TURN_LEFT:
                    switch (arrowDir)
                    {
                    case UP: arrowDir = ArrowDirection.LEFT; break;
                    case DOWN: arrowDir = ArrowDirection.RIGHT; break;
                    case LEFT: arrowDir = ArrowDirection.DOWN; break;
                    case RIGHT: arrowDir = ArrowDirection.UP; break;
                    }
                    
                    break;
                
                case TURN_RIGHT:
                    switch (arrowDir)
                    {
                    case UP: arrowDir = ArrowDirection.RIGHT; break;
                    case DOWN: arrowDir = ArrowDirection.LEFT; break;
                    case LEFT: arrowDir = ArrowDirection.UP; break;
                    case RIGHT: arrowDir = ArrowDirection.DOWN; break;
                    }
                    
                    break;
                    
                case CALL_F1:
                    callFun = 0;
                    break;
                    
                case CALL_F2:
                    callFun = 1;
                    break;
                    
                case CALL_F3:
                    callFun = 2;
                    break;
                    
                case CALL_F4:
                    callFun = 3;
                    break;
                    
                case CALL_F5:
                    callFun = 4;
                    break;
                }
            }
            
            pcSlot++;
            
            if (callFun < 0)
            {
                if (pcSlot == program[pcFunc].length)
                {
                    if (stackPtr > 0)
                    {
                        pcSlot = slotStack[--stackPtr];
                        pcFunc = funcStack[stackPtr];
                    }
                    else
                    {
                        return simsteps;
                    }
                }
            }
            else
            {
                if (pcSlot < program[pcFunc].length)
                {
                    slotStack[stackPtr] = pcSlot;
                    funcStack[stackPtr++] = pcFunc;
                }
                
                pcSlot = 0;
                pcFunc = callFun;
            }
        }
    }
    
    private void printProgram()
    {
        int funIdx = 1;
        
        for (final Instruction[] fun : program)
        {
            System.out.print("F" + funIdx + ": ");
            
            for (final Instruction inst : fun)
            {
                System.out.print(inst);
                System.out.print(" ");
            }
            
            System.out.println();
            
            funIdx++;
        }
    }
}
