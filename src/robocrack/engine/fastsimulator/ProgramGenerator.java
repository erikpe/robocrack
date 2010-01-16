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
    
    public BigInteger totPrograms;
    
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
//        program[0][0] = new Instruction(OpCode.CALL_F2, Condition.ON_ALL);
//        program[0][1] = new Instruction(OpCode.CALL_F1, Condition.ON_ALL);
//        program[1][0] = new Instruction(OpCode.GO_FORWARD, Condition.ON_ALL);
//        program[1][1] = new Instruction(OpCode.CALL_F2, Condition.ON_BLUE);
//        program[1][2] = new Instruction(OpCode.CALL_F3, Condition.ON_ALL);
//        program[2][0] = new Instruction(OpCode.GO_FORWARD, Condition.ON_ALL);
//        program[2][1] = new Instruction(OpCode.CALL_F3, Condition.ON_BLUE);
//        program[2][2] = new Instruction(OpCode.TURN_LEFT, Condition.ON_ALL);
        
//        program[0][0] = new Instruction(OpCode.GO_FORWARD, Condition.ON_BLUE);
//        program[0][1] = new Instruction(OpCode.TURN_RIGHT, Condition.ON_RED);
//        program[0][2] = new Instruction(OpCode.CALL_F2, Condition.ON_ALL);
//        program[1][0] = new Instruction(OpCode.TURN_RIGHT, Condition.ON_RED);
//        program[1][1] = new Instruction(OpCode.GO_FORWARD, Condition.ON_RED);
//        program[1][2] = new Instruction(OpCode.CALL_F1, Condition.ON_BLUE);
        
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
        
        totPrograms = BigInteger.valueOf(instructions.length).pow(totalSlots);
        
        System.out.println("Number of program slots: " + totalSlots);
        System.out.println("Number of possible programs: " + totPrograms);
        
        return tmpProgram;
    }
}
