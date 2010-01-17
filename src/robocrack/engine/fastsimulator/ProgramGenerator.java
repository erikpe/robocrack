package robocrack.engine.fastsimulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.CellPosition;
import robocrack.engine.program.Instruction;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class ProgramGenerator
{
    private final ProgramModel programModel;
    private final BoardModel boardModel;
    
    private final Instruction[] instructions;
    final Instruction[][] program;
    
    public boolean hasNext = true;
    
    public long programsGenerated = 1;
    
    public BigInteger totPrograms;
    
    private final Set<Condition> usedColors;
    
    public ProgramGenerator(final ProgramModel programModel,
            final BoardModel boardModel)
    {
        this.programModel = programModel;
        this.boardModel = boardModel;
        
        this.usedColors = getUsedColors();
        this.instructions = genInstructionTable();
        this.program = genProgram();
        
        initialProgram();
    }
    
    private void initialProgram()
    {
        for (int funcNr = 0; funcNr < program.length; ++funcNr)
        {
            final Instruction[] func = program[funcNr];
            
            for (int slot = 0; slot < func.length; ++slot)
            {
                func[slot] = instructions[instructions.length - 1];
            }
        }
    }
    
    public final void nextProgram()
    {
        for (int funcNr = 0; funcNr < program.length; ++funcNr)
        {
            for (int slot = 0; slot < program[funcNr].length; ++slot)
            {
                if (program[funcNr][slot].id > 0)
                {
                    programsGenerated++;
                    program[funcNr][slot] = instructions[program[funcNr][slot].id - 1];
                    return;
                }
                
                program[funcNr][slot] = instructions[instructions.length - 1];
            }
        }
        
        hasNext = false;
    }
    
    private Set<Condition> getUsedColors()
    {
        final Set<Condition> set = EnumSet.of(Condition.ON_ALL);
        
        for (int y = 0; y < boardModel.height(); ++y)
        {
            for (int x = 0; x < boardModel.width(); ++x)
            {
                switch (boardModel.getColor(CellPosition.make(x, y)))
                {
                case RED: set.add(Condition.ON_RED); break;
                case GREEN: set.add(Condition.ON_GREEN); break;
                case BLUE: set.add(Condition.ON_BLUE); break;
                }
            }
        }
        
        if (programModel.isAllowed(OpCode.PAINT_RED))
        {
            set.add(Condition.ON_RED);
        }
        
        if (programModel.isAllowed(OpCode.PAINT_GREEN))
        {
            set.add(Condition.ON_GREEN);
        }
        
        if (programModel.isAllowed(OpCode.PAINT_BLUE))
        {
            set.add(Condition.ON_BLUE);
        }
        
        return set;
    }
    
    private Instruction[] genInstructionTable()
    {
        final List<Instruction> list = new ArrayList<Instruction>();
        int id = 0;
        
        for (final OpCode opCode : OpCode.values())
        {
            for (final Condition condition : Condition.values())
            {
                Instruction inst = new Instruction(opCode, condition);
                
                if (isApplicable(inst))
                {
                    inst = new Instruction(opCode, condition, id++);
                    list.add(inst);
                }
            }
        }
        
        return list.toArray(new Instruction[list.size()]);
    }
    
    private boolean isApplicable(final Instruction inst)
    {
        if (!programModel.isAllowed(inst.opCode))
        {
            return false;
        }
        else if (inst.opCode == OpCode.NOP && inst.condition != Condition.ON_ALL)
        {
            return false;
        }
        else if (inst.opCode == OpCode.PAINT_RED && inst.condition == Condition.ON_RED)
        {
            return false;
        }
        else if (inst.opCode == OpCode.PAINT_GREEN && inst.condition == Condition.ON_GREEN)
        {
            return false;
        }
        else if (inst.opCode == OpCode.PAINT_BLUE && inst.condition == Condition.ON_BLUE)
        {
            return false;
        }
        
        return usedColors.contains(inst.condition);
    }
    
    private int numFunctions()
    {
        for (int funcNr = 1; funcNr <= 4; ++funcNr)
        {
            if (programModel.getFunctionLength(funcNr + 1) == 0)
            {
                return funcNr;
            }
        }
        
        return 5;
    }
    
    private Instruction[][] genProgram()
    {
        int totalSlots = 0;
        
        final int numFunctions = numFunctions();
        
        final Instruction[][] tmpProgram = new Instruction[numFunctions][];
        
        for (int funcNr = 0; funcNr < tmpProgram.length; ++funcNr)
        {
            final int slots = programModel.getFunctionLength(funcNr + 1);
            tmpProgram[funcNr] = new Instruction[slots];
            
            totalSlots += slots;
        }
        
        totPrograms = BigInteger.valueOf(instructions.length).pow(totalSlots);
        
        System.out.println("Number of program slots: " + totalSlots);
        System.out.println("Number of possible programs: " + totPrograms);
        
        return tmpProgram;
    }
}
