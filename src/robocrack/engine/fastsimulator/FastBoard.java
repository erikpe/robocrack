package robocrack.engine.fastsimulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.board.CellPosition;
import robocrack.engine.program.ProgramModel.Condition;

public class FastBoard
{
    public static class Cell
    {
        public final int up;
        public final int down;
        public final int left;
        public final int right;

        public final Condition origColor;
        public final boolean origHasStar;

        public Condition color;
        public boolean hasStar;

        public final CellPosition pos;

        private Cell(final int up, final int down, final int left,
                final int right, final Condition origColor,
                final boolean origHasStar, final CellPosition pos)
        {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;

            this.origColor = origColor;
            this.origHasStar = origHasStar;

            this.color = origColor;
            this.hasStar = origHasStar;

            this.pos = pos;
        }
    }

    public final Cell[] board;
    public final int numStars;
    public final int arrowDirection;

    public final int[] takenStars;
    public final int[] paintedCells;
    public int starsLeft;
    public int numPaintedCells;

    public FastBoard(final BoardModel boardModel)
    {
        this.board = genBoard(boardModel);
        this.numStars = numStars();
        this.arrowDirection = arrowDirectionNum(boardModel.getArrowDirection());

        this.takenStars = new int[numStars];
        this.paintedCells = new int[1000];
        this.starsLeft = numStars;
        this.numPaintedCells = 0;
    }

    private int arrowDirectionNum(final ArrowDirection direction)
    {
        switch (direction)
        {
        case UP: return 0;
        case RIGHT: return 1;
        case DOWN: return 2;
        case LEFT: return 3;
        default: return -1;
        }
    }

    private Cell[] genBoard(final BoardModel boardModel)
    {
        final Map<CellPosition, Integer> map = new HashMap<CellPosition, Integer>();
        int index = 1;

        for (int y = 0; y < boardModel.height(); ++y)
        {
            for (int x = 0; x < boardModel.width(); ++x)
            {
                final CellPosition pos = CellPosition.make(x, y);
                final CellColor color = boardModel.getColor(pos);
                final boolean hasArrow = pos.equals(boardModel.getArrowPosition());

                if (hasArrow)
                {
                    map.put(pos, 0);
                }
                else if (color != CellColor.NONE)
                {
                    map.put(pos, index++);
                }
            }
        }

        final Cell[] tmpBoard = new Cell[map.size()];

        for (final Entry<CellPosition, Integer> entry : map.entrySet())
        {
            final CellPosition pos = entry.getKey();
            index = entry.getValue().intValue();

            final Condition color = cellColorToCondition(boardModel.getColor(pos));
            final boolean hasStar = boardModel.hasStar(pos);

            final CellPosition upPos = CellPosition.make(pos.x, pos.y - 1);
            final CellPosition downPos = CellPosition.make(pos.x, pos.y + 1);
            final CellPosition leftPos = CellPosition.make(pos.x - 1, pos.y);
            final CellPosition rightPos = CellPosition.make(pos.x + 1, pos.y);

            final int up = map.containsKey(upPos) ? map.get(upPos) : -1;
            final int down = map.containsKey(downPos) ? map.get(downPos) : -1;
            final int left = map.containsKey(leftPos) ? map.get(leftPos) : -1;
            final int right = map.containsKey(rightPos) ? map.get(rightPos) : -1;

            tmpBoard[index] = new Cell(up, down, left, right, color, hasStar, pos);
        }

        return tmpBoard;
    }

    private Condition cellColorToCondition(final CellColor color)
    {
        switch (color)
        {
        case RED: return Condition.ON_RED;
        case GREEN: return Condition.ON_GREEN;
        case BLUE: return Condition.ON_BLUE;
        case NONE: return Condition.ON_ALL;
        default: return null;
        }
    }

    private int numStars()
    {
        int stars = 0;

        for (final Cell cell : board)
        {
            if (cell.hasStar)
            {
                stars++;
            }
        }

        return stars;
    }

    public void reset()
    {
        while (starsLeft < numStars)
        {
            board[takenStars[starsLeft++]].hasStar = true;
        }

        while (numPaintedCells > 0)
        {
            numPaintedCells--;
            board[paintedCells[numPaintedCells]].color = board[paintedCells[numPaintedCells]].origColor;
        }
    }
}
