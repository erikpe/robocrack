package robocrack.engine.board;

import java.util.Observable;

public class BoardModel extends Observable
{
    public static enum CellColor
    {
        RED,
        GREEN,
        BLUE,
        NONE
    }

    public static enum ArrowDirection
    {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
    
    private static final ArrowDirection ARROW_DEFAULT_DIRECTION = ArrowDirection.RIGHT;
    private static final CellPosition ARROW_DEFAULT_POSITION = CellPosition.make(
            0, 0);

    private final int width;
    private final int height;

    private final Cell[][] board;

    private ArrowDirection arrowDirection;
    private Cell currentCell;
    private int nrStars;

    public BoardModel(final int width, final int height)
    {
        this.width = width;
        this.height = height;
        this.board = makeBoard(width, height);
        this.arrowDirection = ARROW_DEFAULT_DIRECTION;
        this.currentCell = cellAt(ARROW_DEFAULT_POSITION);
    }

    private static Cell[][] makeBoard(final int width, final int height)
    {
        final Cell[][] tmpBoard = new Cell[width][];

        for (int x = 0; x < width; ++x)
        {
            tmpBoard[x] = new Cell[height];

            for (int y = 0; y < height; ++y)
            {
                tmpBoard[x][y] = new Cell(CellPosition.make(x, y));
            }
        }

        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                if (x > 0) {
                    tmpBoard[x][y].leftNeighbour = tmpBoard[x - 1][y];
                }

                if (x < width - 1) {
                    tmpBoard[x][y].rightNeighbour = tmpBoard[x + 1][y];
                }

                if (y > 0) {
                    tmpBoard[x][y].upNeighbour = tmpBoard[x][y - 1];
                }

                if (y < height - 1) {
                    tmpBoard[x][y].downNeighbour = tmpBoard[x][y + 1];
                }
            }
        }

        return tmpBoard;
    }

    private Cell cellAt(final CellPosition coord)
    {
        return board[coord.x][coord.y];
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    public Cell getCurrentCell()
    {
        return currentCell;
    }

    public boolean hasStar(final CellPosition cellPosition)
    {
        return cellAt(cellPosition).hasStar;
    }

    public void setStar(final CellPosition cellPosition, final boolean star)
    {
        if (cellAt(cellPosition).getColor() != CellColor.NONE)
        {
            cellAt(cellPosition).hasStar = star;
        }

        setChanged();
        notifyObservers(cellPosition);
    }

    public void setColor(final CellPosition cellPosition, final CellColor color)
    {
        cellAt(cellPosition).setColor(color);

        if (color == CellColor.NONE) {
            cellAt(cellPosition).hasStar = false;
        }

        setChanged();
        notifyObservers(cellPosition);
    }

    public CellColor getColor(final CellPosition cellPosition)
    {
        return cellAt(cellPosition).color;
    }

    public CellPosition arrowCoordinate()
    {
        return currentCell.cellPosition;
    }

    public ArrowDirection arrowDirection()
    {
        return arrowDirection;
    }

    public void setArrow(final CellPosition cellPosition)
    {
        final CellPosition oldCoordinate = arrowCoordinate();
        currentCell = cellAt(cellPosition);

        setChanged();
        notifyObservers(oldCoordinate);
        setChanged();
        notifyObservers(arrowCoordinate());
    }

    public void goForward()
    {
        final CellPosition oldPosition = currentCell.cellPosition;
        
        switch (arrowDirection)
        {
        case LEFT:
            currentCell = currentCell.leftNeighbour;
            break;

        case RIGHT:
            currentCell = currentCell.rightNeighbour;
            break;

        case UP:
            currentCell = currentCell.upNeighbour;
            break;

        case DOWN:
            currentCell = currentCell.downNeighbour;
            break;
        }

        if (currentCell == null)// || currentCell.color == CellColor.NONE)
        {
            throw new RuntimeException("Outside of the board");
        }

        if (currentCell.hasStar)
        {
            currentCell.hasStar = false;
            nrStars--;

            if (nrStars == 0)
            {
                throw new RuntimeException("Finished!");
            }
        }
        
        setChanged();
        notifyObservers(oldPosition);
        setChanged();
        notifyObservers(currentCell.cellPosition);
    }

    public void turnLeft()
    {
        switch (arrowDirection)
        {
        case LEFT:
            arrowDirection = ArrowDirection.DOWN;
            break;

        case RIGHT:
            arrowDirection = ArrowDirection.UP;
            break;

        case UP:
            arrowDirection = ArrowDirection.LEFT;
            break;

        case DOWN:
            arrowDirection = ArrowDirection.RIGHT;
            break;
        }

        setChanged();
        notifyObservers(currentCell.cellPosition);
    }

    public void turnRight()
    {
        switch (arrowDirection)
        {
        case LEFT:
            arrowDirection = ArrowDirection.UP;
            break;

        case RIGHT:
            arrowDirection = ArrowDirection.DOWN;
            break;

        case UP:
            arrowDirection = ArrowDirection.RIGHT;
            break;

        case DOWN:
            arrowDirection = ArrowDirection.LEFT;
            break;
        }

        setChanged();
        notifyObservers(currentCell.cellPosition);
    }

    public void clear()
    {
        for (int y = 0; y < height(); ++y)
        {
            for (int x = 0; x < width(); ++x)
            {
                setColor(CellPosition.make(x, y), CellColor.NONE);
            }
        }
    }
}
