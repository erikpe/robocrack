package robocrack.engine.program;

import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class Instruction
{
    public OpCode opCode;
    public Condition condition;
    public int id;
    
    public Instruction(final OpCode opCode, final Condition condition)
    {
        this(opCode, condition, 0);
    }
    
    public Instruction(final OpCode opCode, final Condition condition, final int id)
    {
        this.opCode = opCode;
        this.condition = condition;
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "<" + opCode + ", " + condition + ">";
    }
}
