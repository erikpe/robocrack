package robocrack.engine.fastsimulator;

import java.math.BigInteger;
import java.util.EnumSet;

import robocrack.engine.program.Instruction;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class ProgramGenerator
{
    private final ProgramModel programModel;
    
    private final Instruction[] instructions;
    final Instruction[][] program;
    
    public boolean hasNext = true;
    
    public long programsGenerated = 1;
    
    public ProgramGenerator(final ProgramModel programModel)
    {
        this.programModel = programModel;
        
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
    
    private Instruction[] genInstructionTable()
    {
        final EnumSet<OpCode> opCodes = EnumSet.allOf(OpCode.class);
        final EnumSet<Condition> conditions = EnumSet.allOf(Condition.class);        
        
        for (final OpCode opCode : OpCode.values())
        {
            if (!programModel.isAllowed(opCode))
            {
                opCodes.remove(opCode);
            }
        }
        
        opCodes.remove(OpCode.NOP);
        
        final Instruction[] tmpInst = new Instruction[opCodes.size()
                * conditions.size() + 1];
        
        int id = 0;
        
        for (final OpCode opCode : opCodes)
        {
            for (final Condition condition : conditions)
            {
                tmpInst[id] = new Instruction(opCode, condition, id);
                id++;
            }
        }
        
        tmpInst[id] = new Instruction(OpCode.NOP, Condition.ON_ALL, id);
        
        System.out.println("Number of possible instructions: " + tmpInst.length);
        
        return tmpInst;
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
        
        System.out.println("Number of program slots: " + totalSlots);
        System.out.println("Number of possible programs: "
                + BigInteger.valueOf(instructions.length).pow(totalSlots));
        
        return tmpProgram;
    }
}
