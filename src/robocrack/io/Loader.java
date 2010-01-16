package robocrack.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import robocrack.engine.board.BoardModel;
import robocrack.engine.board.CellPosition;
import robocrack.engine.board.BoardModel.ArrowDirection;
import robocrack.engine.board.BoardModel.CellColor;
import robocrack.engine.program.InstructionPosition;
import robocrack.engine.program.ProgramModel;
import robocrack.engine.program.ProgramModel.Condition;
import robocrack.engine.program.ProgramModel.OpCode;

public class Loader
{
    private static String URL_BASE = "http://robozzle.com/js/play.aspx?puzzle=";
    
    private static String readString(final BufferedReader reader)
            throws IOException
    {
        final StringBuilder sb = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null)
        {
            sb.append(line).append('\n');
        }
        
        return sb.toString();
    }
    
    private static String readURL(final URL url) throws IOException
    {
        final InputStream inputStream = url.openStream();
        final InputStreamReader reader = new InputStreamReader(inputStream);
        final BufferedReader bufReader = new BufferedReader(reader);
        
        return readString(bufReader);
    }
    
    public static void loadFromWeb(final int id, final BoardModel boardModel,
            final ProgramModel programModel)
    {
        try
        {
            final URL url = new URL(URL_BASE + id);
            final String content = readURL(url);
            final String puzzleStr = extractPuzzle(content);
            System.out.println(puzzleStr);
            loadFromString(puzzleStr, boardModel, programModel);
        }
        catch (final MalformedURLException e)
        {
            System.out.println("Error in puzzle URL");
        }
        catch (final IOException e)
        {
            System.out.println("Error downloading puzzle");
        }
    }
    
    private static String extractPuzzle(final String content)
    {
        final int beginIndex = content.indexOf("var puzzles = ");
        final int endIndex = content.indexOf("}];", beginIndex) + 3;
        
        return content.substring(beginIndex, endIndex);
    }
    
    private static void loadFromString(final String puzzleStr,
            final BoardModel boardModel, final ProgramModel programModel)
    {
        boardModel.setArrowPosition(getArrowPosition(puzzleStr));
        boardModel.setArrowDirection(getArrowDir(puzzleStr));
        setFunctions(puzzleStr, programModel);
        loadBoard(puzzleStr, boardModel);
    }
    
    private static void loadBoard(final String puzzleStr,
            final BoardModel boardModel)
    {
        int beginIndex = puzzleStr.indexOf("board");
        beginIndex = puzzleStr.indexOf('"', beginIndex) + 1;
        int endIndex = puzzleStr.indexOf('"', beginIndex);
        
        final String boardStr = puzzleStr.substring(beginIndex, endIndex);

        boardModel.clear();
        
        for (int y = 0; y < 12; ++y)
        {
            for (int x = 0; x < 16; ++x)
            {
                final CellPosition pos = CellPosition.make(x, y);
                final boolean star;
                final CellColor color;
                
                switch (boardStr.charAt(x + 16 * y))
                {
                case 'R': star = true; color = CellColor.RED; break;
                case 'r': star = false; color = CellColor.RED; break;
                case 'G': star = true; color = CellColor.GREEN; break;
                case 'g': star = false; color = CellColor.GREEN; break;
                case 'B': star = true; color = CellColor.BLUE; break;
                case 'b': star = false; color = CellColor.BLUE; break;
                default: star = false; color = CellColor.NONE; break;
                }
                
                boardModel.setColor(pos, color);
                boardModel.setStar(pos, star);
            }
        }
    }
    
    private static void setFunctions(final String puzzleStr,
            final ProgramModel programModel)
    {
        int beginIndex = puzzleStr.indexOf("subs");
        beginIndex = puzzleStr.indexOf('[', beginIndex) + 1;
        int endIndex = puzzleStr.indexOf(']', beginIndex);
        
        final String[] funcStrings = puzzleStr.substring(beginIndex, endIndex)
                .split(",");
        
        for (int i = 5; i > 1; --i)
        {
            programModel.setFunctionLength(i, 0);
        }
        programModel.setFunctionLength(1, 1);
        programModel.setCondition(InstructionPosition.make(1, 0), Condition.ON_ALL);
        programModel.setOpCode(InstructionPosition.make(1, 0), OpCode.NOP);
        
        for (int i = 0; i < 5; ++i)
        {
            final int func = Integer.valueOf(funcStrings[i].trim());
            programModel.setFunctionLength(i + 1, func);
        }
    }
    
    private static ArrowDirection getArrowDir(final String puzzleStr)
    {
        int beginIndex = puzzleStr.indexOf("robotDir");
        beginIndex = puzzleStr.indexOf(':', beginIndex) + 1;
        int endIndex = puzzleStr.indexOf(',', beginIndex);
        
        final int dir = Integer.valueOf(puzzleStr.substring(beginIndex,
                endIndex).trim());
        
        switch (dir)
        {
        case 0: return ArrowDirection.RIGHT;
        case 1: return ArrowDirection.DOWN;
        case 2: return ArrowDirection.LEFT;
        case 3: return ArrowDirection.UP;
        default: return null;
        }
    }
    
    private static CellPosition getArrowPosition(final String puzzleStr)
    {
        int beginIndex;
        int endIndex;
        
        beginIndex = puzzleStr.indexOf("robotCol");
        beginIndex = puzzleStr.indexOf(':', beginIndex) + 1;
        endIndex = puzzleStr.indexOf(',', beginIndex);
        
        final int col = Integer.valueOf(puzzleStr.substring(beginIndex,
                endIndex).trim());
        
        beginIndex = puzzleStr.indexOf("robotRow");
        beginIndex = puzzleStr.indexOf(':', beginIndex) + 1;
        endIndex = puzzleStr.indexOf(',', beginIndex);
        
        final int row = Integer.valueOf(puzzleStr.substring(beginIndex,
                endIndex).trim());
        
        return CellPosition.make(col, row);
    }
}
