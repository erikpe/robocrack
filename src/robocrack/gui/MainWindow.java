package robocrack.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import robocrack.engine.board.BoardModel;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.board.BoardButtonPane;
import robocrack.gui.board.BoardPane;
import robocrack.gui.program.FunctionButtonPane;
import robocrack.gui.program.ProgramPane;
import robocrack.gui.simulator.SimulatorButtonPane;
import robocrack.gui.simulator.StackPane;

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
    public MainWindow(final BoardModel boardModel,
            final ProgramModel programModel, final Simulator simulator)
    {
        final GuiModel guiModel = new GuiModel();
        
        final BoardButtonPane boardButtonPane = new BoardButtonPane(guiModel,
                simulator);
        final BoardPane boardPane = new BoardPane(boardModel, guiModel);
        final FunctionButtonPane functionButtonPane = new FunctionButtonPane(
                guiModel, simulator);
        final ProgramPane programPane = new ProgramPane(programModel, guiModel,
                simulator);
        final SimulatorButtonPane simButtonPane = new SimulatorButtonPane(
                simulator);
        final StackPane stackPane = new StackPane(simulator, guiModel, 15, 3);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        getContentPane().add(boardButtonPane);
        getContentPane().add(boardPane);
        getContentPane().add(functionButtonPane);
        getContentPane().add(programPane);
        getContentPane().add(simButtonPane);
        getContentPane().add(stackPane);
        
        pack();
        setMinimumSize(getPreferredSize());
    }
}
