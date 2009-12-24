package robocrack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import robocrack.engine.board.Board;
import robocrack.engine.board.CellColor;
import robocrack.engine.board.Coordinate;
import robocrack.engine.board.Direction;

@SuppressWarnings("serial")
public class CellComponent extends SquareComponent
{
    final static int CELL_WIDTH = 21;
    final static int CELL_HEIGHT = CELL_WIDTH;
    
    private final Board board;
    private final Coordinate coordinate;
    private final GuiState guiState;
    
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
    
    CellComponent(final Board board, final Coordinate coordinate,
            final GuiState guiState)
    {
        this.board = board;
        this.coordinate = coordinate;
        this.guiState = guiState;
        
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
        switch(board.getColor(coordinate))
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
        if (!board.hasStar(coordinate))
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
        if (!coordinate.equals(board.arrowCoordinate()))
        {
            return;
        }
        
        final Polygon arrowPolygon = getArrowPolygon(board.arrowDirection());
        
        g.setColor(Color.YELLOW);
        g.fillPolygon(arrowPolygon);
        g.setColor(Color.BLACK);
        g.drawPolygon(arrowPolygon);
    }
    
    private Polygon getArrowPolygon(final Direction direction)
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
    void leftButtonPressed()
    {
        guiState.setStar(!board.hasStar(coordinate));
        leftButtonAction();
    }
    
    @Override
    void leftButtonEntered()
    {
        leftButtonAction();
    }
    
    private void leftButtonAction()
    {
        switch(guiState.selectedBoardButton())
        {
        case RED_BUTTON:
            board.setColor(coordinate, CellColor.RED);
            break;
            
        case GREEN_BUTTON:
            board.setColor(coordinate, CellColor.GREEN);
            break;
            
        case BLUE_BUTTON:
            board.setColor(coordinate, CellColor.BLUE);
            break;
            
        case STAR_BUTTON:
            board.setStar(coordinate, guiState.getStar());
            break;
            
        case ARROW_BUTTON:
            updateArrow();
            break;
        }
    }
    
    @Override
    void rightButtonPressed()
    {
       rightButtonAction();
    }
    
    @Override
    void rightButtonEntered()
    {
        rightButtonAction();
    }
    
    private void rightButtonAction()
    {
        board.setColor(coordinate, CellColor.NONE);
    }
    
    private void updateArrow()
    {
        if (coordinate.equals(board.arrowCoordinate()))
        {
            board.turnRight();
        }
        else
        {
            board.setArrow(coordinate);
        }
    }
}
