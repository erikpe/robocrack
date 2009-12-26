package robocrack.main;

import robocrack.engine.board.BoardModel;
import robocrack.engine.program.ProgramModel;
import robocrack.gui.MainWindow;

public class Main
{
    public static void main(final String[] argv)
    {
        final BoardModel boardModel = new BoardModel(22, 15);
        final ProgramModel programModel = new ProgramModel();
        
        final MainWindow window = new MainWindow(boardModel, programModel);
        //final MainWindow window2 = new MainWindow(boardModel, programModel);
        window.setVisible(true);
        //window2.setVisible(true);
    }
}
