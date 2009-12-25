package robocrack.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import robocrack.engine.board.BoardModel;
import robocrack.engine.program.ProgramModel;
import robocrack.gui.board.BoardButtonPane;
import robocrack.gui.board.BoardPane;
import robocrack.gui.program.FunctionButtonPane;
import robocrack.gui.program.ProgramPane;

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
    public MainWindow(final BoardModel boardModel, final ProgramModel programModel)
    {
        final GuiModel guiModel = new GuiModel();
        
        final BoardButtonPane boardButtonPane = new BoardButtonPane(guiModel);
        final BoardPane boardPane = new BoardPane(boardModel, guiModel);
        final FunctionButtonPane functionButtonPane = new FunctionButtonPane(
                guiModel);
        final ProgramPane programPane = new ProgramPane(programModel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        
        getContentPane().add(boardButtonPane);
        getContentPane().add(boardPane);
        getContentPane().add(functionButtonPane);
        getContentPane().add(programPane);
        
        pack();
        setMinimumSize(getPreferredSize());
    }
}
