package robocrack.gui.board;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import robocrack.engine.board.BoardEditor;
import robocrack.engine.board.CellPosition;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class BoardPane extends JComponent implements Observer
{
    private final static int CELL_SPACING = 1;
    
    private final BoardEditor boardEditor;
    private final GuiModel guiModel;
    
    private final CellComponent[][] cells;
    
    public BoardPane(final BoardEditor boardEditor, final GuiModel guiState)
    {
        this.boardEditor = boardEditor;
        this.guiModel = guiState;
        this.cells = new CellComponent[boardEditor.width()][boardEditor.height()];
        
        initialize();
    }
    
    private void initialize()
    {
        for (int y = 0; y < boardEditor.height(); ++y)
        {
            for (int x = 0; x < boardEditor.width(); ++x)
            {
                final CellPosition cellPosition = CellPosition.make(x, y);
                CellComponent cc = new CellComponent(boardEditor, cellPosition, guiModel);
                cells[x][y] = cc;
                add(cc);
                
                final int xBounds = x
                        * (CellComponent.CELL_WIDTH + CELL_SPACING);
                final int yBounds = y
                        * (CellComponent.CELL_HEIGHT + CELL_SPACING);
                
                cc.setBounds(xBounds, yBounds, CellComponent.CELL_WIDTH,
                        CellComponent.CELL_HEIGHT);
            }
        }
        
        final int width = boardEditor.width()
                * (CellComponent.CELL_WIDTH + CELL_SPACING) - CELL_SPACING;
        final int height = boardEditor.height()
                * (CellComponent.CELL_HEIGHT + CELL_SPACING) - CELL_SPACING;
        
        setPreferredSize(new Dimension(width, height));
        
        boardEditor.addObserver(this);
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof CellPosition)
        {
            final CellPosition position = (CellPosition) arg;
            cells[position.x][position.y].repaint();
        }
    }
}
