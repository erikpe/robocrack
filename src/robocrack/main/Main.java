package robocrack.main;

import robocrack.engine.board.BoardModel;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.simulator.Simulator;
import robocrack.gui.MainWindow;

public class Main
{
    public static void main(final String[] argv)
    {
        final BoardModel boardModel = new BoardModel(23, 15);
        final ProgramModel programModel = new ProgramModel();
        final Simulator simulator = new Simulator(boardModel, programModel);

        final MainWindow window = new MainWindow(boardModel, programModel, simulator);
        //final MainWindow window2 = new MainWindow(boardModel, programModel, simulator);

        window.setVisible(true);
        //window2.setVisible(true);
    }
}
