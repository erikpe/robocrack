package robocrack.main;

import robocrack.engine.board.BoardModel;
import robocrack.gui.MainWindow;

public class Main
{
    public static void main(final String[] argv)
    {
        final BoardModel board = new BoardModel(22, 15);
        final MainWindow window = new MainWindow(board);
        window.setVisible(true);
    }
}
