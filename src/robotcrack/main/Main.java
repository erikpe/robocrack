package robotcrack.main;

import robotcrack.engine.board.Board;
import robotcrack.gui.MainWindow;

public class Main
{
    public static void main(final String[] argv)
    {
        final Board board = new Board(20, 20);
        final MainWindow window = new MainWindow(board);
        window.setVisible(true);
    }
}
