package robocrack.gui.board;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.CellPosition;
import robocrack.gui.GuiModel;

@SuppressWarnings("serial")
public class BoardPane extends JComponent implements Observer
{
    private final static int CELL_SPACING = 1;
    
    private final BoardModel board;
    private final GuiModel guiModel;
    
    private final CellComponent[][] cells;
    
    public BoardPane(final BoardModel board, final GuiModel guiState)
    {
        this.board = board;
        this.guiModel = guiState;
        this.cells = new CellComponent[board.width()][board.height()];
        
        initialize();
    }
    
    private void initialize()
    {
        for (int y = 0; y < board.height(); ++y)
        {
            for (int x = 0; x < board.width(); ++x)
            {
                final CellPosition cellPosition = CellPosition.make(x, y);
                CellComponent cc = new CellComponent(board, cellPosition, guiModel);
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
        
        final int width = board.width()
                * (CellComponent.CELL_WIDTH + CELL_SPACING) - CELL_SPACING;
        final int height = board.height()
                * (CellComponent.CELL_HEIGHT + CELL_SPACING) - CELL_SPACING;
        
        setPreferredSize(new Dimension(width, height));
        
        board.addObserver(this);
    }
    
    private CellComponent cellComponentAt(final CellPosition cellPosition)
    {
        return cells[cellPosition.x][cellPosition.y];
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof CellPosition)
        {
            cellComponentAt((CellPosition) arg).repaint();
        }
    }
}
