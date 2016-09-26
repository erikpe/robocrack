package robocrack.engine.board;

import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.BoardModel.CellColor;

public interface BoardEditor extends BoardViewer
{
    public void setStar(final CellPosition cellPosition, final boolean star);

    public void setColor(final CellPosition cellPosition, final CellColor color);

    public void setArrowPosition(final CellPosition cellPosition);

    public void setArrowDirection(final ArrowDirection direction);
}
