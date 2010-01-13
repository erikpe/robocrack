package robocrack.engine.fastsimulator;

import java.util.HashMap;

import robocrack.engine.board.BoardModel;
import robocrack.engine.fastsimulator.FastBoard.Cell;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;

public class FastSimulator
{
    private static final int MAX_SIMSTEPS = 1000;
    
    private final ProgramGenerator programGenerator;
    private final FastBoard fastBoard;
    
    private final Instruction[][] program;
    private final Cell[] board;
    
    private final int[] funcStack = new int[MAX_SIMSTEPS];
    private final int[] slotStack = new int[MAX_SIMSTEPS];
    private final int[][] stateStack = new int[MAX_SIMSTEPS][MAX_SIMSTEPS + 1];
    
    private HashMap<Integer, Object> stateSet;
    
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
        long lastPrint = startTime;
        
        long testedPrograms = 0;
        long simsteps = 0;
        
        do
        {
            simsteps += simulate();
            testedPrograms++;
            
            if (testedPrograms % 1000 == 0)
            {
                long now = System.currentTimeMillis();
                
                if (now - lastPrint > 5000.0)
                {
                    final double time = (now - startTime) / 1000.0;
                    
                    System.out.println(testedPrograms + " tried in " + time
                            + " sec (" + (testedPrograms / time) + " progs/s)");
                    
                    System.out.println("Total number of simulation steps: "
                            + simsteps + " (" + (simsteps / time)
                            + " steps/s, avg: "
                            + (((double) simsteps) / testedPrograms)
                            + " steps/prog)");
                    
                    lastPrint = now;
                }
            }
            
            if (fastBoard.starsLeft == 0)
            {
                final long stopTime = System.currentTimeMillis();
                final double time = (stopTime - startTime) / 1000.0;
                
                System.out.println("Found solution after trying "
                        + testedPrograms + " programs in " + time + " sec ("
                        + (testedPrograms / time) + " progs/s)");
                
                System.out.println("Total number of simulation steps: "
                        + simsteps + " (" + (simsteps / time)
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
        int arrowDir = fastBoard.arrowDirection;
        
        int pcFunc = 0;
        int pcSlot = 0;
        
        int simsteps = 0;
        int stackPtr = 0;
        
        Condition condition;
        int callFun;
        
        int state;
        stateStack[0][0] = 1;
        
        stateSet = new HashMap<Integer, Object>();
        
        while (true)
        {
            state = arrowPos ^ (arrowDir << 12) ^ (pcFunc << 14)
                    ^ (pcSlot << 17);
            
            if (stateSet.containsKey(state))
            {
                return simsteps;
            }
            
            stateStack[stackPtr][stateStack[stackPtr][0]++] = state;
            stateSet.put(state, null);
            
            if (++simsteps == MAX_SIMSTEPS)
            {
                return simsteps;
            }
            
            callFun = -1;
            
            condition = program[pcFunc][pcSlot].condition;
            if (condition == Condition.ON_ALL || condition == board[arrowPos].color)
            {
                switch (program[pcFunc][pcSlot].opCode)
                {
                case GO_FORWARD:
                    switch (arrowDir)
                    {
                    case 0: arrowPos = board[arrowPos].up; break;
                    case 1: arrowPos = board[arrowPos].right; break;
                    case 2: arrowPos = board[arrowPos].down; break;
                    case 3: arrowPos = board[arrowPos].left; break;
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
                    arrowDir = (arrowDir + 3) % 4;
                    break;
                
                case TURN_RIGHT:
                    arrowDir = (arrowDir + 1) % 4;
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
                        while (stateStack[stackPtr][0] > 1)
                        {
                            stateSet.remove(stateStack[stackPtr][--stateStack[stackPtr][0]]);
                        }
                        
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
                    
                    stateStack[stackPtr][0] = 1;
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
