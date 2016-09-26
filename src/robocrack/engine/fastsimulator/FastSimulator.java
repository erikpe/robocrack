package robocrack.engine.fastsimulator;

import java.math.BigInteger;
import java.util.Observable;

import robocrack.engine.board.BoardModel;
import robocrack.engine.fastsimulator.FastBoard.Cell;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;

public class FastSimulator extends Observable
{
    private static final int MAX_SIMSTEPS = 1000;

    public final ProgramGenerator programGenerator;
    private final FastBoard fastBoard;

    public final Instruction[][] program;
    private final Cell[] board;

    private final int[] funcStack = new int[MAX_SIMSTEPS];
    private final int[] slotStack = new int[MAX_SIMSTEPS];

    private final int[] stateStack;
    private final int[] stateStackPtr;
    private int stateStackCntr;

    public final BigInteger totPrograms;
    public long totTestedPrograms = 0;
    public long totSimsteps = 0;

    public boolean stop = false;
    public boolean solutionFound = false;

    public FastSimulator(final BoardModel boardModel,
            final ProgramModel programModel)
    {
        this.programGenerator = new ProgramGenerator(programModel, boardModel);
        this.fastBoard = new FastBoard(boardModel);

        this.program = programGenerator.program;
        this.board = fastBoard.board;

        this.stateStack = new int[MAX_SIMSTEPS];
        this.stateStackPtr = new int[MAX_SIMSTEPS];

        totPrograms = programGenerator.totPrograms;
    }

    public void bruteForce()
    {
        do
        {
            totSimsteps += simulate();

            totTestedPrograms++;

            if (fastBoard.starsLeft == 0)
            {
                printProgram();
                solutionFound = true;

                setChanged();
                notifyObservers();

                return;
            }

            fastBoard.reset();
            programGenerator.nextProgram();
        } while (!stop && programGenerator.hasNext);

        setChanged();
        notifyObservers();
    }

    private int simulate()
    {
        int arrowPos = 0;
        int arrowDir = fastBoard.arrowDirection;

        int pcFunc = 0;
        int pcSlot = 0;

        int simsteps = 0;
        int stackPtr = 0;

        int callFun;
        int i;
        int state;
        stateStackPtr[0] = 0;
        stateStackCntr = 0;

        while (true)
        {
            state = arrowPos ^ (arrowDir << 12) ^ (pcFunc << 14)
                    ^ (pcSlot << 17);

            for (i = 0; i < stateStackPtr[stateStackCntr]; ++i)
            {
                if (stateStack[i] == state)
                {
                    return simsteps;
                }
            }

            stateStack[stateStackPtr[stateStackCntr]++] = state;

            if (++simsteps == MAX_SIMSTEPS)
            {
                return simsteps;
            }

            callFun = -1;

            if (program[pcFunc][pcSlot].condition == Condition.ON_ALL
                    || program[pcFunc][pcSlot].condition == board[arrowPos].color)
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

                case PAINT_RED:
                    if (board[arrowPos].color != Condition.ON_RED)
                    {
                        stateStackCntr = 0;
                        stateStackPtr[0] = 0;
                        board[arrowPos].color = Condition.ON_RED;
                        fastBoard.paintedCells[fastBoard.numPaintedCells++] = arrowPos;
                    }
                    break;

                case PAINT_GREEN:
                    if (board[arrowPos].color != Condition.ON_GREEN)
                    {
                        stateStackCntr = 0;
                        stateStackPtr[0] = 0;
                        board[arrowPos].color = Condition.ON_GREEN;
                        fastBoard.paintedCells[fastBoard.numPaintedCells++] = arrowPos;
                    }
                    break;

                case PAINT_BLUE:
                    if (board[arrowPos].color != Condition.ON_BLUE)
                    {
                        stateStackCntr = 0;
                        stateStackPtr[0] = 0;
                        board[arrowPos].color = Condition.ON_BLUE;
                        fastBoard.paintedCells[fastBoard.numPaintedCells++] = arrowPos;
                    }
                    break;
                case NOP:
                    break;
                }
            }

            pcSlot++;

            if (callFun < 0)
            {
                if (pcSlot == program[pcFunc].length)
                {
                    if (stackPtr == 0)
                    {
                        return simsteps;
                    }

                    pcSlot = slotStack[--stackPtr];
                    pcFunc = funcStack[stackPtr];

                    if (stateStackCntr > 0)
                    {
                        stateStackCntr--;
                    }
                    else
                    {
                        stateStackPtr[0] = 0;
                    }
                }
            }
            else
            {
                if (pcSlot < program[pcFunc].length)
                {
                    slotStack[stackPtr] = pcSlot;
                    funcStack[stackPtr++] = pcFunc;

                    stateStackCntr++;
                    stateStackPtr[stateStackCntr] = stateStackPtr[stateStackCntr - 1];
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
