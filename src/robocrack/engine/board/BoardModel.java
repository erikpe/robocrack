package robocrack.engine.board;

import java.util.Observable;

public class BoardModel extends Observable implements BoardEditor,
        BoardSimulator
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
        DOWN;
        
        public ArrowDirection turnLeft()
        {
            switch (this)
            {
            case RIGHT: return UP;
            case DOWN: return RIGHT;
            case LEFT: return DOWN;
            case UP: return LEFT;
            }
            
            return null;
        }
        
        public ArrowDirection turnRight()
        {
            switch (this)
            {
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
            case LEFT: return UP;
            case UP: return RIGHT;
            }
            
            return null;
        }
    }
    
    public static enum Mode
    {
        EDIT,
        SIMULATE
    }
    
    private static final ArrowDirection ARROW_DEFAULT_DIRECTION = ArrowDirection.RIGHT;
    private static final CellPosition ARROW_DEFAULT_POSITION = CellPosition.make(
            0, 0);

    private final int width;
    private final int height;
    
    private Mode mode;
    
    private final Cell[][] board;
    
    private ArrowDirection startArrowDirection;
    private Cell startCell;
    private int nrStars;

    private ArrowDirection simArrowDirection;
    private Cell simCurrentCell;
    private int simNrStars;
    
    public BoardModel(final int width, final int height)
    {
        this.width = width;
        this.height = height;
        this.mode = Mode.EDIT;
        
        this.board = makeBoard();
        this.startArrowDirection = ARROW_DEFAULT_DIRECTION;
        this.startCell = cellAt(ARROW_DEFAULT_POSITION);
    }
    
    private Cell[][] makeBoard()
    {
        final Cell[][] tmpBoard = new Cell[width()][];

        for (int x = 0; x < width(); ++x)
        {
            tmpBoard[x] = new Cell[height()];

            for (int y = 0; y < height(); ++y)
            {
                tmpBoard[x][y] = new Cell(CellPosition.make(x, y));
            }
        }
        
        for (int x = 0; x < width(); ++x)
        {
            for (int y = 0; y < height(); ++y)
            {
                if (x > 0)
                {
                    tmpBoard[x][y].leftNeighbour = tmpBoard[x - 1][y];
                }

                if (x < width() - 1)
                {
                    tmpBoard[x][y].rightNeighbour = tmpBoard[x + 1][y];
                }

                if (y > 0)
                {
                    tmpBoard[x][y].upNeighbour = tmpBoard[x][y - 1];
                }

                if (y < height() - 1)
                {
                    tmpBoard[x][y].downNeighbour = tmpBoard[x][y + 1];
                }
            }
        }

        return tmpBoard;
    }
    
    public Mode getMode()
    {
        return mode;
    }
    
    public void startSimulation()
    {
        mode = Mode.SIMULATE;
        
        simArrowDirection = startArrowDirection;
        simCurrentCell = startCell;
        simNrStars = nrStars;
        
        for (int y = 0; y < height(); ++y)
        {
            for (int x = 0; x < width(); ++x)
            {
                board[x][y].startSimulation();
            }
        }
    }
    
    public void resetSimulation()
    {
        mode = Mode.EDIT;
        
        for (int y = 0; y < height(); ++y)
        {
            for (int x = 0; x < width(); ++x)
            {
                if (board[x][y].resetSimulation())
                {
                    setChanged();
                    notifyObservers(board[x][y].cellPosition);
                }
            }
        }
        
        setChanged();
        notifyObservers(simCurrentCell.cellPosition);
        
        setChanged();
        notifyObservers(startCell.cellPosition);
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
    
    public boolean hasStar(final CellPosition cellPosition)
    {
        final Cell cell = cellAt(cellPosition);
        return mode == Mode.EDIT ? cell.hasStar : cell.simHasStar;
    }
    
    public void setStar(final CellPosition cellPosition, final boolean star)
    {
        if (mode != Mode.EDIT)
        {
            return;
        }
        
        if (cellAt(cellPosition).color != CellColor.NONE)
        {
            cellAt(cellPosition).hasStar = star;
        }
        
        setChanged();
        notifyObservers(cellPosition);
    }
    
    public void setColor(final CellPosition cellPosition, final CellColor color)
    {
        if (mode != Mode.EDIT)
        {
            return;
        }
        
        cellAt(cellPosition).color = color;
        
        if (color == CellColor.NONE)
        {
            cellAt(cellPosition).hasStar = false;
        }
        
        setChanged();
        notifyObservers(cellPosition);
    }
    
    public void simPaintColor(final CellColor color)
    {
        if (mode != Mode.SIMULATE)
        {
            return;
        }
        
        simCurrentCell.simColor = color;
        
        setChanged();
        notifyObservers(simCurrentCell.cellPosition);
    }
    
    public CellColor getColor(final CellPosition cellPosition)
    {
        final Cell cell = cellAt(cellPosition);
        return mode == Mode.EDIT ? cell.color : cell.simColor;
    }
    
    public CellPosition getArrowPosition()
    {
        return mode == Mode.EDIT ? startCell.cellPosition
                : simCurrentCell.cellPosition;
    }
    
    public ArrowDirection getArrowDirection()
    {
        return mode == Mode.EDIT ? startArrowDirection : simArrowDirection;
    }
    
    public Cell getCurrentCell()
    {
        return mode == Mode.EDIT ? startCell : simCurrentCell;
    }
    
    public void setArrowPosition(final CellPosition cellPosition)
    {
        if (mode != Mode.EDIT)
        {
            return;
        }
        
        final CellPosition oldPosition = getArrowPosition();
        startCell = cellAt(cellPosition);
        
        setChanged();
        notifyObservers(oldPosition);
        setChanged();
        notifyObservers(getArrowPosition());
    }
    
    public void setArrowDirection(final ArrowDirection direction)
    {
        if (mode != Mode.EDIT)
        {
            return;
        }
        
        startArrowDirection = direction;
        
        setChanged();
        notifyObservers(getArrowPosition());
    }
    
    private Cell getNeighbourCell(final Cell cell,
            final ArrowDirection arrowDirection)
    {
        switch (arrowDirection)
        {
        case RIGHT: return cell.rightNeighbour;
        case DOWN: return cell.downNeighbour;
        case LEFT: return cell.leftNeighbour;
        case UP: return cell.upNeighbour;
        }
        
        return null;
    }
    
    public void simGoForward()
    {
        if (mode != Mode.SIMULATE)
        {
            return;
        }
        
        final CellPosition oldPosition = simCurrentCell.cellPosition;
        simCurrentCell = getNeighbourCell(simCurrentCell, getArrowDirection());
        
        if (simCurrentCell == null)// || currentCell.color == CellColor.NONE)
        {
            throw new RuntimeException("Outside of the board");
        }
        
        if (simCurrentCell.simHasStar)
        {
            simCurrentCell.simHasStar = false;
            simNrStars--;

            if (simNrStars == 0)
            {
                throw new RuntimeException("Finished!");
            }
        }
        
        setChanged();
        notifyObservers(oldPosition);
        setChanged();
        notifyObservers(simCurrentCell.cellPosition);
    }

    public void simTurnLeft()
    {
        if (mode != Mode.SIMULATE)
        {
            return;
        }
        
        simArrowDirection = simArrowDirection.turnLeft();
        setChanged();
        notifyObservers(simCurrentCell.cellPosition);
    }

    public void simTurnRight()
    {
        if (mode != Mode.SIMULATE)
        {
            return;
        }
        
        simArrowDirection = simArrowDirection.turnRight();
        setChanged();
        notifyObservers(simCurrentCell.cellPosition);
    }

    public void clear()
    {
        if (mode == Mode.SIMULATE)
        {
            return;
        }
        
        for (int y = 0; y < height(); ++y)
        {
            for (int x = 0; x < width(); ++x)
            {
                setColor(CellPosition.make(x, y), CellColor.NONE);
            }
        }
    }
}
