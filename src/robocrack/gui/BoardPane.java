package robocrack.gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.Coordinate;

@SuppressWarnings("serial")
public class BoardPane extends JComponent implements Observer
{
    private final static int CELL_SPACING = 1;
    
    private final BoardModel board;
    private final GuiModel guiModel;
    
    private final CellComponent[][] cells;
    
    BoardPane(final BoardModel board, final GuiModel guiState)
    {
        this.board = board;
        this.guiModel = guiState;
        
        this.cells = new CellComponent[board.width()][board.height()];
        buildGrid();
        board.addObserver(this);
    }
    
    private void buildGrid()
    {
        for (int y = 0; y < board.height(); ++y)
        {
            for (int x = 0; x < board.width(); ++x)
            {
                final Coordinate coordinate = Coordinate.make(x, y);
                CellComponent cc = new CellComponent(board, coordinate,
                        guiModel);
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
    }
    
    private CellComponent cellComponentAt(final Coordinate coordinate)
    {
        return cells[coordinate.x][coordinate.y];
    }
    
    @Override
    public void update(final Observable observable, final Object arg)
    {
        if (arg instanceof Coordinate)
        {
            cellComponentAt((Coordinate) arg).repaint();
        }
    }
}
