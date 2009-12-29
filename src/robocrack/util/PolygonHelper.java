package robocrack.util;

import java.awt.Polygon;

public class PolygonHelper
{
    public static enum PolygonType
    {
        LEFT_ARROW(xLeftArrow, yLeftArrow),
        RIGHT_ARROW(xRightArrow, yRightArrow),
        UP_ARROW(xUpArrow, yUpArrow),
        DOWN_ARROW(xDownArrow, yDownArrow),
        
        GO_FORWARD(xGoForward, yGoForward),
        TURN_LEFT(xTurnLeft, yTurnLeft),
        TURN_RIGHT(xTurnRight, yTurnRight);
        
        final double[] x;
        final double[] y;
        
        private PolygonType(final double[] x, final double[] y)
        {
            this.x = x;
            this.y = y;
        }
    }
    
    private static final double[] xLeftArrow = { .8, .8, .2 };
    private static final double[] yLeftArrow = { .2, .8, .5 };
    
    private static final double[] xRightArrow = { .2, .2, .8 };
    private static final double[] yRightArrow = { .8, .2, .5 };
    
    private static final double[] xUpArrow = { .8, .2, .5 };
    private static final double[] yUpArrow = { .8, .8, .2 };
    
    private static final double[] xDownArrow = { .2, .8, .5 };
    private static final double[] yDownArrow = { .2, .2, .8 };
    
    private static final double[] xGoForward =
        { .45, .45, .3, .5, .7, .55, .55 };
    private static final double[] yGoForward =
        { .85, .35, .35, .15, .35, .35, .85 };
    
    private static final double[] xTurnLeft =
        { .55, .55, .35, .35, .15, .35, .35, .67, .67 };
    private static final double[] yTurnLeft =
        { .85, .4, .4, .55, .35, .15, .3, .3, .85 };
    
    private static final double[] xTurnRight =
        { .33, .33, .65, .65, .85, .65, .65, .45, .45 };
    private static final double[] yTurnRight =
        { .85, .3, .3, .15, .35, .55, .4, .4, .85 };
    
    public static Polygon makePolygon(final PolygonType type, int width,
            int height)
    {
        final int x = Math.max(0, (width - height) / 2);
        final int y = Math.max(0, (height - width) / 2);
        
        width = Math.min(width, height);
        height = width;
        
        return makePolygon(type.x, type.y, x, y, width, height);
    }
    
    private static Polygon makePolygon(final double[] xNorm,
            final double[] yNorm, final int x, final int y, final int width,
            final int height)
    {
        assert xNorm.length == yNorm.length;
        
        final int[] xOut = new int[xNorm.length];
        final int[] yOut = new int[xNorm.length];
        
        for (int i = 0; i < xNorm.length; ++i)
        {
            xOut[i] = (int) (width * xNorm[i] + x);
            yOut[i] = (int) (height * yNorm[i] + y);
        }
        
        return new Polygon(xOut, yOut, xNorm.length);
    }
}
