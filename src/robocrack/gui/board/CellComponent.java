package robocrack.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import robocrack.engine.board.BoardEditor;
import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.board.CellPosition;
import robocrack.gui.GuiModel;
import robocrack.gui.common.SquareComponent;
import robocrack.util.PolygonHelper;
import robocrack.util.PolygonHelper.PolygonType;

@SuppressWarnings("serial")
public class CellComponent extends SquareComponent
{
    final static int CELL_WIDTH = 21;
    final static int CELL_HEIGHT = CELL_WIDTH;

    private final BoardEditor boardEditor;
    private final CellPosition cellPosition;
    private final GuiModel guiModel;

    CellComponent(final BoardEditor boardEditor, final CellPosition cellPosition,
            final GuiModel guiState)
    {
        this.boardEditor = boardEditor;
        this.cellPosition = cellPosition;
        this.guiModel = guiState;
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
        switch(boardEditor.getColor(cellPosition))
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
        paintBackground(g);
        paintBorder(g);
        paintArrow(g);
        paintStar(g);
    }

    private void paintArrow(final Graphics g)
    {
        if (!cellPosition.equals(boardEditor.getArrowPosition()))
        {
            return;
        }

        final PolygonType polygonType = getPolygonType(boardEditor
                .getArrowDirection());
        final Polygon polygon = PolygonHelper.makePolygon(polygonType, width(),
                height());

        g.setColor(Color.YELLOW);
        g.fillPolygon(polygon);
        g.setColor(Color.BLACK);
        g.drawPolygon(polygon);
    }

    private void paintStar(final Graphics g)
    {
        if (!boardEditor.hasStar(cellPosition))
        {
            return;
        }

        g.setColor(Color.YELLOW);
        g.fillOval(width() / 3, height() / 3, width() / 3, height() / 3);
        g.setColor(Color.BLACK);
        g.drawOval(width() / 3, height() / 3, width() / 3, height() / 3);
    }

    private PolygonType getPolygonType(final ArrowDirection direction)
    {
        switch (direction)
        {
        case LEFT: return PolygonType.LEFT_ARROW;
        case RIGHT: return PolygonType.RIGHT_ARROW;
        case UP: return PolygonType.UP_ARROW;
        case DOWN: return PolygonType.DOWN_ARROW;
        default: return null;
        }
    }

    @Override
    protected void leftButtonPressed()
    {
        guiModel.setStar(!boardEditor.hasStar(cellPosition));
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
            boardEditor.setColor(cellPosition, CellColor.RED);
            break;

        case GREEN_BUTTON:
            boardEditor.setColor(cellPosition, CellColor.GREEN);
            break;

        case BLUE_BUTTON:
            boardEditor.setColor(cellPosition, CellColor.BLUE);
            break;

        case STAR_BUTTON:
            boardEditor.setStar(cellPosition, guiModel.getStar());
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
        boardEditor.setColor(cellPosition, CellColor.NONE);
    }

    private void updateArrow()
    {
        if (cellPosition.equals(boardEditor.getArrowPosition()))
        {
            boardEditor.setArrowDirection(boardEditor.getArrowDirection().turnRight());
        }
        else
        {
            boardEditor.setArrowPosition(cellPosition);
        }
    }
}
