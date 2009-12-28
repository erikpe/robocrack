package robocrack.engine.program;

import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class Instruction
{
    public OpCode opCode;
    public Condition condition;
    
    Instruction(final OpCode opCode, final Condition condition)
    {
        this.opCode = opCode;
        this.condition = condition;
    }
}
