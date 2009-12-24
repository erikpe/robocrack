package robotcrack.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import robotcrack.engine.board.Board;

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
    public MainWindow(final Board board)
    {
        final GuiState guiState = new GuiState();
        final BoardButtonPane buttonPane = new BoardButtonPane(guiState);
        final BoardPane boardPane = new BoardPane(board, guiState);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        getContentPane().add(buttonPane);
        getContentPane().add(boardPane);
        
        pack();
    }
}
