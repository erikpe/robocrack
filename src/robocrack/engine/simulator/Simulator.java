package robocrack.engine.simulator;

import java.util.List;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.Cell;

public class Simulator
{
    private List<Instruction> f1;
    private List<Instruction> f2;
    private List<Instruction> f3;
    private List<Instruction> f4;
    private List<Instruction> f5;
    
    private BoardModel board;
    
    public Simulator(List<List<Instruction>> program)
    {
        this.f1 = program.get(0);
        this.f2 = program.get(1);
        this.f3 = program.get(2);
        this.f4 = program.get(3);
        this.f5 = program.get(4);
    }
    
    public void simulate()
    {
        simulate(f1);
    }
    
    private void simulate(final List<Instruction> f)
    {
        int pos = 0;
        
        Cell cell = board.getCurrentCell();
        
        Instruction instruction;
        
        do
        {
            instruction = f.get(pos);
            pos++;
        } while(instruction.condition != Condition.ON_ALL);
        
        switch(f.get(pos).action)
        {
        case CALL_F1:
            simulate(f1);
            break;
            
        case CALL_F2:
            simulate(f2);
            break;
            
//        case FORWARD:
//            board.forward();
//            cell = board.getCurrentCell();
//            break;
//            
//        case LEFT:
//            board.left();
            
        }
    }
}
