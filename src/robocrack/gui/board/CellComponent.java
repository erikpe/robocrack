package robocrack.gui.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.CellPosition;
import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.Cell.CellColor;
import robocrack.gui.GuiModel;
import robocrack.gui.common.SquareComponent;

@SuppressWarnings("serial")
public class CellComponent extends SquareComponent
{
    final static int CELL_WIDTH = 21;
    final static int CELL_HEIGHT = CELL_WIDTH;
    
    private final BoardModel board;
    private final CellPosition cellPosition;
    private final GuiModel guiModel;
    
    private static final int X1 = CELL_WIDTH / 5;
    private static final int X2 = CELL_WIDTH / 2;
    private static final int X3 = CELL_WIDTH - CELL_WIDTH / 5;
    private static final int Y1 = CELL_HEIGHT / 5;
    private static final int Y2 = CELL_HEIGHT / 2;
    private static final int Y3 = CELL_HEIGHT - CELL_HEIGHT / 5;
    
    private static final Polygon leftArrow = new Polygon(
            new int[] { X3, X3, X1 }, new int[] { Y1, Y3, Y2 }, 3);
    
    private static final Polygon rightArrow = new Polygon(
            new int[] { X1, X1, X3 }, new int[] { Y3, Y1, Y2 }, 3);
    
    private static final Polygon upArrow = new Polygon(
            new int[] { X3, X1, X2 }, new int[] { Y3, Y3, Y1 }, 3);
    
    private static final Polygon downArrow = new Polygon(
            new int[] { X1, X3, X2 }, new int[] { Y1, Y1, Y3 }, 3);
    
    CellComponent(final BoardModel board, final CellPosition cellPosition,
            final GuiModel guiState)
    {
        this.board = board;
        this.cellPosition = cellPosition;
        this.guiModel = guiState;
        
        setSize(new Dimension(width(), height()));
    }
    
    @Override
    protected int width()
    {
        return CELL_WIDTH;
    }
    
    @Override
    protected int height()
    {
        return CELL_HEIGHT;
    }
    
    @Override
    protected Color getBackgroundColor()
    {
        switch(board.getColor(cellPosition))
        {
        case NONE: return Color.LIGHT_GRAY;
        case RED: return Color.RED;
        case BLUE: return Color.BLUE;
        case GREEN: return Color.GREEN;
        default: return null;
        }
    }
    
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        paintArrow(g);
        paintStar(g);
    }
    
    private void paintStar(final Graphics g)
    {
        if (!board.hasStar(cellPosition))
        {
            return;
        }
        
        g.setColor(Color.YELLOW);
        g.fillOval(width() / 3, height() / 3, width() / 3, height() / 3);
        g.setColor(Color.BLACK);
        g.drawOval(width() / 3, height() / 3, width() / 3, height() / 3);
    }
    
    private void paintArrow(final Graphics g)
    {
        if (!cellPosition.equals(board.arrowCoordinate()))
        {
            return;
        }
        
        final Polygon arrowPolygon = getArrowPolygon(board.arrowDirection());
        
        g.setColor(Color.YELLOW);
        g.fillPolygon(arrowPolygon);
        g.setColor(Color.BLACK);
        g.drawPolygon(arrowPolygon);
    }
    
    private Polygon getArrowPolygon(final ArrowDirection direction)
    {
        switch(direction)
        {
        case LEFT: return leftArrow;
        case RIGHT: return rightArrow;
        case UP: return upArrow;
        case DOWN: return downArrow;
        default: return null;
        }
    }
    
    @Override
    protected void leftButtonPressed()
    {
        guiModel.setStar(!board.hasStar(cellPosition));
        leftButtonAction();
    }
    
    @Override
    protected void leftButtonEntered()
    {
        leftButtonAction();
    }
    
    private void leftButtonAction()
    {
        switch(guiModel.selectedBoardButton())
        {
        case RED_BUTTON:
            board.setColor(cellPosition, CellColor.RED);
            break;
            
        case GREEN_BUTTON:
            board.setColor(cellPosition, CellColor.GREEN);
            break;
            
        case BLUE_BUTTON:
            board.setColor(cellPosition, CellColor.BLUE);
            break;
            
        case STAR_BUTTON:
            board.setStar(cellPosition, guiModel.getStar());
            break;
            
        case ARROW_BUTTON:
            updateArrow();
            break;
        }
    }
    
    @Override
    protected void rightButtonPressed()
    {
       rightButtonAction();
    }
    
    @Override
    protected void rightButtonEntered()
    {
        rightButtonAction();
    }
    
    private void rightButtonAction()
    {
        board.setColor(cellPosition, CellColor.NONE);
    }
    
    private void updateArrow()
    {
        if (cellPosition.equals(board.arrowCoordinate()))
        {
            board.turnRight();
        }
        else
        {
            board.setArrow(cellPosition);
        }
    }
}
