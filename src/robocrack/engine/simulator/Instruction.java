package robocrack.engine.simulator;

public class Instruction
{
    public final Action action;
    public final Condition condition;
    
    public Instruction(final Action action, final Condition condition)
    {
        this.action = action;
        this.condition = condition;
    }
}
