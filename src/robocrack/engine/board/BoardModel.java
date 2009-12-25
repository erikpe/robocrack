package robocrack.engine.board;

import java.util.Observable;

import robocrack.engine.board.Cell.CellColor;

public class BoardModel extends Observable {
    public static enum ArrowDirection {
        LEFT, RIGHT, UP, DOWN
    }

    private static final ArrowDirection ARROW_DEFAULT_DIRECTION = ArrowDirection.RIGHT;
    private static final Coordinate ARROW_DEFAULT_COORDINATE = Coordinate.make(
            0, 0);

    private final int width;
    private final int height;

    private final Cell[][] board;

    private ArrowDirection arrowDirection;
    private Cell currentCell;
    private int nrStars;

    public BoardModel(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.board = makeBoard(width, height);
        this.arrowDirection = ARROW_DEFAULT_DIRECTION;
        this.currentCell = cellAt(ARROW_DEFAULT_COORDINATE);
    }

    private static Cell[][] makeBoard(final int width, final int height) {
        final Cell[][] tmpBoard = new Cell[width][];

        for (int x = 0; x < width; ++x) {
            tmpBoard[x] = new Cell[height];

            for (int y = 0; y < height; ++y) {
                tmpBoard[x][y] = new Cell(Coordinate.make(x, y));
            }
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
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

    private Cell cellAt(final Coordinate coord) {
        return board[coord.x][coord.y];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public boolean hasStar(final Coordinate coordinate) {
        return cellAt(coordinate).hasStar;
    }

    public void setStar(final Coordinate coordinate, final boolean star) {
        if (cellAt(coordinate).getColor() != CellColor.NONE) {
            cellAt(coordinate).hasStar = star;
        }

        setChanged();
        notifyObservers(coordinate);
    }

    public void setColor(final Coordinate coordinate, final CellColor color) {
        cellAt(coordinate).setColor(color);

        if (color == CellColor.NONE) {
            cellAt(coordinate).hasStar = false;
        }

        setChanged();
        notifyObservers(coordinate);
    }

    public CellColor getColor(final Coordinate coordinate) {
        return cellAt(coordinate).color;
    }

    public Coordinate arrowCoordinate() {
        return currentCell.coordinate;
    }

    public ArrowDirection arrowDirection() {
        return arrowDirection;
    }

    public void setArrow(final Coordinate coordinate) {
        final Coordinate oldCoordinate = arrowCoordinate();
        currentCell = cellAt(coordinate);

        setChanged();
        notifyObservers(oldCoordinate);
        setChanged();
        notifyObservers(arrowCoordinate());
    }

    public void goForward() {
        switch (arrowDirection) {
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

        if (currentCell == null || currentCell.color == CellColor.NONE) {
            throw new RuntimeException("Outside of the board");
        }

        if (currentCell.hasStar) {
            currentCell.hasStar = false;
            nrStars--;

            if (nrStars == 0) {
                throw new RuntimeException("Finished!");
            }
        }
    }

    public void turnLeft() {
        switch (arrowDirection) {
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
        notifyObservers(currentCell.coordinate);
    }

    public void turnRight() {
        switch (arrowDirection) {
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
        notifyObservers(currentCell.coordinate);
    }

    public void clear() {
        for (int y = 0; y < height(); ++y) {
            for (int x = 0; x < width(); ++x) {
                setColor(Coordinate.make(x, y), CellColor.NONE);
            }
        }

        arrowDirection = ARROW_DEFAULT_DIRECTION;
        setArrow(ARROW_DEFAULT_COORDINATE);
    }
}
