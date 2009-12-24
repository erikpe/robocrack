package robocrack.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import robocrack.engine.board.BoardModel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
    public MainWindow(final BoardModel board)
    {
        final GuiModel guiModel = new GuiModel();
        final BoardButtonPane buttonPane = new BoardButtonPane(guiModel);
        final BoardPane boardPane = new BoardPane(board, guiModel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        getContentPane().add(buttonPane);
        getContentPane().add(boardPane);
        
        pack();
    }
}
